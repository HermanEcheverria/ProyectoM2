pipeline {
    agent any

    environment {
        PROJECT_NAME  = 'proyecto-m2'       // nombre base del proyecto
        SONARQUBE_ENV = 'SonarQubeServer'   // nombre del server Sonar configurado en Jenkins
    }

    tools {
        maven 'Maven'
        jdk   'java-17'
    }

    stages {
        stage('Checkout') {
            steps { checkout scm }
        }

        stage('Build & Unit Tests (Backend)') {
            steps {
                dir('backend') {
                    sh 'mvn clean verify -DskipTests=false'
                    // Sanity check: asegurarnos que JaCoCo generó el XML
                    sh '''
                      ls -lah target/site/jacoco || true
                      test -f target/site/jacoco/jacoco.xml || { 
                        echo "❌ No se encontró backend/target/site/jacoco/jacoco.xml. Revisa JaCoCo en el POM.";
                        exit 1;
                      }
                    '''
                }
            }
        }

        stage('SonarQube Analysis (Repo completo)') {
  steps {
    withSonarQubeEnv("${SONARQUBE_ENV}") {
      withCredentials([string(credentialsId: 'tokensonar', variable: 'SONAR_TOKEN')]) {
        sh '''
          set -e
          SCANNER_DIR="${WORKSPACE}/.sonar/sonar-scanner"
          SCANNER_BIN="${SCANNER_DIR}/bin/sonar-scanner"

          # Descarga y prepara sonar-scanner en el WORKSPACE si no existe
          if [ ! -x "$SCANNER_BIN" ]; then
            echo "🔧 Preparando sonar-scanner en $SCANNER_DIR ..."
            mkdir -p "$SCANNER_DIR"
            curl -sSL -o "${WORKSPACE}/.sonar/sonar-scanner.zip" \
              https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-5.0.1.3006-linux.zip

            cd "${WORKSPACE}/.sonar"
            # Usamos 'jar xf' (Java) para descomprimir el ZIP sin necesidad de unzip/apt
            jar xf sonar-scanner.zip
            rm sonar-scanner.zip
            mv sonar-scanner-5.0.1.3006-linux sonar-scanner
          fi

          export PATH="${SCANNER_DIR}/bin:$PATH"

          # Emulación multirama en Community: projectKey por rama
          PK="${PROJECT_NAME}-${BRANCH_NAME}"

          "${SCANNER_BIN}" \
            -Dsonar.projectKey="${PK}" \
            -Dsonar.projectName="Proyecto M2 (${BRANCH_NAME})" \
            -Dsonar.sources=backend,. \
            -Dsonar.exclusions=**/node_modules/**,**/dist/**,**/build/**,backend/target/** \
            -Dsonar.java.binaries=backend/target/classes \
            -Dsonar.coverage.jacoco.xmlReportPaths=backend/target/site/jacoco/jacoco.xml \
            -Dsonar.token="$SONAR_TOKEN"
        '''
      }
    }
  }
}


        stage('Quality Gate') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                    echo "✅ SonarQube OK (Quality Gate) para ${env.BRANCH_NAME}"
                }
            }
        }
    }

    post {
        failure {
            // Si tu SMTP aún no está configurado, puedes comentar este bloque
            mail to: 'magic@productowner.com, herman@unis.edu.gt',
                 subject: "❌ Falló pipeline en rama ${env.BRANCH_NAME}",
                 body: "El pipeline falló en la etapa ${env.STAGE_NAME}. Revisar Jenkins."
        }
    }
}
