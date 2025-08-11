pipeline {
  agent any

  environment {
    PROJECT_NAME  = 'proyecto-m2'
    SONARQUBE_ENV = 'SonarQubeServer'
  }

  tools {
    maven 'Maven'
    jdk   'java-17'
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Install Frontend deps (root)') {
      steps {
        script {
          nodejs('Node 20') {
            sh '''
              if [ -f package.json ]; then
                echo "Instalando dependencias del front en la raíz..."
                npm ci --no-audit --no-fund
              else
                echo "No hay package.json en la raíz."
              fi
            '''
          }
        }
      }
    }

    stage('Build & Unit Tests (Backend)') {
      steps {
        dir('backend') {
          sh 'mvn -q clean verify -DskipTests=false'
          sh 'mvn -q jacoco:report'
          sh '''
            test -f target/site/jacoco/jacoco.xml || {
              echo "No se encontró backend/target/site/jacoco/jacoco.xml";
              exit 1;
            }
          '''
        }
      }
    }

    stage('SonarQube Analysis - Backend (con cobertura)') {
      steps {
        script {
          def scannerHome = tool 'SonarScanner'
          withSonarQubeEnv("${SONARQUBE_ENV}") {
            withCredentials([string(credentialsId: 'tokensonar', variable: 'SONAR_TOKEN')]) {
              withEnv(["PATH+SONAR=${scannerHome}/bin"]) {
                // evita ambigüedad de report-task.txt
                sh 'rm -rf .scannerwork backend/.scannerwork || true'
                dir('backend') {
                  def branchSafe = (env.BRANCH_NAME ?: 'local').replaceAll('[^A-Za-z0-9_\\-\\.:]', '-')
                  def key   = "${PROJECT_NAME}-backend-${branchSafe}"
                  def pname = "${PROJECT_NAME} :: Backend [${env.BRANCH_NAME}]"
                  withEnv(["SONAR_PROJECT_KEY=${key}", "SONAR_PROJECT_NAME=${pname}"]) {
                    // análisis
                    sh 'sonar-scanner -Dsonar.token=$SONAR_TOKEN -Dsonar.projectKey=$SONAR_PROJECT_KEY -Dsonar.projectName="$SONAR_PROJECT_NAME"'

                    // --- CE: renombrar rama principal a dev/uat/main para que la UI muestre la rama correcta ---
                    sh '''
                      TARGET="${BRANCH_NAME:-main}"
                      case "$TARGET" in
                        dev|uat|main) ;;
                        *) TARGET="main" ;;
                      esac
                      if [ "$TARGET" != "main" ]; then
                        echo "Renombrando rama principal de Sonar a: $TARGET ..."
                        code=$(curl -s -o /tmp/rename_backend.json -w "%{http_code}" -u $SONAR_TOKEN: \
                          -X POST "$SONAR_HOST_URL/api/project_branches/rename" \
                          --data-urlencode "project=$SONAR_PROJECT_KEY" \
                          --data-urlencode "name=$TARGET")
                        echo "HTTP $code"
                        [ "$code" -lt 400 ] || (echo "WARN: rename branch falló (backend)"; cat /tmp/rename_backend.json || true)
                      fi
                    '''
                  }
                }
              }
            }
          }
        }
      }
    }

    stage('Quality Gate - Backend') {
      steps {
        dir('backend') {
          timeout(time: 10, unit: 'MINUTES') {
            waitForQualityGate abortPipeline: true
            echo "Quality Gate BACKEND OK"
          }
        }
      }
    }

    stage('SonarQube Analysis - Frontend (sin cobertura)') {
      when { expression { fileExists('sonar-project.properties') && fileExists('package.json') } }
      steps {
        script {
          def scannerHome = tool 'SonarScanner'
          withSonarQubeEnv("${SONARQUBE_ENV}") {
            withCredentials([string(credentialsId: 'tokensonar', variable: 'SONAR_TOKEN')]) {
              withEnv(["PATH+SONAR=${scannerHome}/bin"]) {
                sh 'rm -rf .scannerwork backend/.scannerwork || true'
                def branchSafe = (env.BRANCH_NAME ?: 'local').replaceAll('[^A-Za-z0-9_\\-\\.:]', '-')
                def key   = "${PROJECT_NAME}-frontend-${branchSafe}"
                def pname = "${PROJECT_NAME} :: Frontend [${env.BRANCH_NAME}]"
                withEnv(["SONAR_PROJECT_KEY=${key}", "SONAR_PROJECT_NAME=${pname}"]) {
                  // análisis
                  sh 'sonar-scanner -Dsonar.token=$SONAR_TOKEN -Dsonar.projectKey=$SONAR_PROJECT_KEY -Dsonar.projectName="$SONAR_PROJECT_NAME"'

                  // --- CE: renombrar rama principal a dev/uat/main ---
                  sh '''
                    TARGET="${BRANCH_NAME:-main}"
                    case "$TARGET" in
                      dev|uat|main) ;;
                      *) TARGET="main" ;;
                    esac
                    if [ "$TARGET" != "main" ]; then
                      echo "Renombrando rama principal de Sonar a: $TARGET ..."
                      code=$(curl -s -o /tmp/rename_frontend.json -w "%{http_code}" -u $SONAR_TOKEN: \
                        -X POST "$SONAR_HOST_URL/api/project_branches/rename" \
                        --data-urlencode "project=$SONAR_PROJECT_KEY" \
                        --data-urlencode "name=$TARGET")
                      echo "HTTP $code"
                      [ "$code" -lt 400 ] || (echo "WARN: rename branch falló (frontend)"; cat /tmp/rename_frontend.json || true)
                    fi
                  '''
                }
              }
            }
          }
        }
      }
    }

    stage('Quality Gate - Frontend') {
      when { expression { fileExists('sonar-project.properties') && fileExists('package.json') } }
      steps {
        timeout(time: 10, unit: 'MINUTES') {
          waitForQualityGate abortPipeline: true
          echo "Quality Gate FRONTEND OK"
        }
      }
    }
  }

  post {
    failure {
      script {
        try {
          mail to: 'hecheverria@unis.edu.gt',
               subject: "Falló pipeline en rama ${env.BRANCH_NAME}",
               body: "El pipeline falló en la etapa ${env.STAGE_NAME}. Revisar Jenkins."
        } catch (e) {
          echo "No se pudo enviar correo: ${e}"
        }
      }
    }
    unstable {
      script {
        try {
          mail to: 'hecheverria@unis.edu.gt',
               subject: "Pipeline UNSTABLE en ${env.BRANCH_NAME}",
               body: "El pipeline quedó UNSTABLE en la etapa ${env.STAGE_NAME}. Revisar Jenkins."
        } catch (e) {
          echo "No se pudo enviar correo: ${e}"
        }
      }
    }
  }
}
