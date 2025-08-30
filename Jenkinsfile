pipeline {
  agent any

  options {
    timestamps()
    ansiColor('xterm')
    disableConcurrentBuilds()
  }

  environment {
    PROJECT_NAME  = 'proyecto-m2'
    SONARQUBE_ENV = 'SonarQubeServer'
  }

  tools {
    maven 'Maven'
    jdk   'java-17'
    // NodeJS se usa dentro de script { nodejs('Node 20') { ... } }
  }

  stages {

    stage('Checkout') {
      steps { checkout scm }
    }

    /********************
     * === PR GATE ===
     * En Pull Requests: Lint Frontend + Unit Tests Backend + Sonar (Backend y Frontend) + Quality Gates
     ********************/
    stage('PR: Lint Frontend') {
      when { changeRequest() }
      steps {
        script {
          nodejs('Node 20') {
            sh '''
              set -euxo pipefail
              if [ -f package.json ] && grep -q '"lint"' package.json; then
                npm ci --no-audit --no-fund
                npm run lint
              else
                echo "No hay script lint en package.json, se omite"
              fi
            '''
          }
        }
      }
    }

    stage('PR: Unit Tests Backend + Jacoco') {
      when { changeRequest() }
      steps {
        dir('backend') {
          sh '''
            set -euxo pipefail
            mvn -q clean verify -DskipTests=false
            mvn -q jacoco:report
            test -f target/site/jacoco/jacoco.xml || {
              echo "No se encontró backend/target/site/jacoco/jacoco.xml";
              exit 1;
            }
          '''
        }
      }
    }

    stage('PR: SonarQube Analysis - Backend (con cobertura)') {
      when { changeRequest() }
      steps {
        script {
          def scannerHome = tool 'SonarScanner'
          withSonarQubeEnv("${SONARQUBE_ENV}") {
            withCredentials([string(credentialsId: 'tokensonar', variable: 'SONAR_TOKEN')]) {
              withEnv(["PATH+SONAR=${scannerHome}/bin"]) {
                dir('backend') {
                  sh '''
                    set -euo pipefail
                    rm -rf .scannerwork || true
                    KEY="${PROJECT_NAME}-backend-pr-${CHANGE_ID}"

                    # Usa backend/sonar-project.properties (define sonar.projectName).
                    # Pasamos coverage explícitamente por CLI.
                    sonar-scanner \
                      -Dsonar.token="$SONAR_TOKEN" \
                      -Dsonar.projectKey="$KEY" \
                      -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                  '''
                }
              }
            }
          }
        }
      }
    }

    stage('PR: Quality Gate - Backend') {
      when { changeRequest() }
      steps {
        dir('backend') {
          timeout(time: 10, unit: 'MINUTES') {
            waitForQualityGate abortPipeline: true
            echo "Quality Gate BACKEND (PR) OK"
          }
        }
      }
    }

    stage('PR: SonarQube Analysis - Frontend (sin cobertura)') {
      when {
        allOf {
          changeRequest()
          expression { fileExists('sonar-project.properties') && fileExists('package.json') }
        }
      }
      steps {
        script {
          def scannerHome = tool 'SonarScanner'
          withSonarQubeEnv("${SONARQUBE_ENV}") {
            withCredentials([string(credentialsId: 'tokensonar', variable: 'SONAR_TOKEN')]) {
              withEnv(["PATH+SONAR=${scannerHome}/bin"]) {
                sh '''
                  set -euo pipefail
                  rm -rf .scannerwork || true
                  KEY="${PROJECT_NAME}-frontend-pr-${CHANGE_ID}"

                  # Usa sonar-project.properties de la raíz (define sonar.projectName).
                  sonar-scanner \
                    -Dsonar.token="$SONAR_TOKEN" \
                    -Dsonar.projectKey="$KEY"
                '''
              }
            }
          }
        }
      }
    }

    stage('PR: Quality Gate - Frontend') {
      when {
        allOf {
          changeRequest()
          expression { fileExists('sonar-project.properties') && fileExists('package.json') }
        }
      }
      steps {
        timeout(time: 10, unit: 'MINUTES') {
          waitForQualityGate abortPipeline: true
          echo "Quality Gate FRONTEND (PR) OK"
        }
      }
    }

    /********************
     * === RAMAS REALES (dev / uat / prod) ===
     * Build + Tests + Jacoco + Sonar + Quality Gate + Deploy por entorno
     ********************/
    stage('Build & Unit Tests (Backend)') {
      when { not { changeRequest() } }
      steps {
        dir('backend') {
          sh '''
            set -euxo pipefail
            mvn -q clean verify -DskipTests=false
            mvn -q jacoco:report
            test -f target/site/jacoco/jacoco.xml || {
              echo "No se encontró backend/target/site/jacoco/jacoco.xml";
              exit 1;
            }
          '''
        }
      }
    }

    stage('Install Frontend deps (root)') {
      when { not { changeRequest() } }
      steps {
        script {
          nodejs('Node 20') {
            sh '''
              set -euxo pipefail
              if [ -f package.json ]; then
                npm ci --no-audit --no-fund
              else
                echo "No hay package.json en la raíz."
              fi
            '''
          }
        }
      }
    }

    stage('SonarQube Analysis - Backend (con cobertura)') {
      when {
        allOf {
          not { changeRequest() }
          expression { ['dev','uat','prod'].contains(env.BRANCH_NAME ?: 'prod') }
        }
      }
      steps {
        script {
          def scannerHome = tool 'SonarScanner'
          withSonarQubeEnv("${SONARQUBE_ENV}") {
            withCredentials([string(credentialsId: 'tokensonar', variable: 'SONAR_TOKEN')]) {
              withEnv(["PATH+SONAR=${scannerHome}/bin"]) {
                dir('backend') {
                  sh '''
                    set -euo pipefail
                    rm -rf .scannerwork || true

                    RAW="${BRANCH_NAME:-prod}"
                    TARGET_ENV="$RAW"

                    KEY="${PROJECT_NAME}-backend-${TARGET_ENV}"
                    EXTS=""
                    if [ "$TARGET_ENV" = "prod" ]; then
                      git fetch --tags --force >/dev/null 2>&1 || true
                      VER=$(git describe --tags --always 2>/dev/null || echo "$BUILD_NUMBER")
                      EXTS="$EXTS -Dsonar.projectVersion=${VER}"
                    fi

                    sonar-scanner \
                      -Dsonar.token="$SONAR_TOKEN" \
                      -Dsonar.projectKey="$KEY" \
                      -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                      $EXTS
                  '''
                }
              }
            }
          }
        }
      }
    }

    stage('Quality Gate - Backend') {
      when {
        allOf {
          not { changeRequest() }
          expression { ['dev','uat','prod'].contains(env.BRANCH_NAME ?: 'prod') }
        }
      }
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
      when {
        allOf {
          not { changeRequest() }
          expression { fileExists('sonar-project.properties') && fileExists('package.json') }
          expression { ['dev','uat','prod'].contains(env.BRANCH_NAME ?: 'prod') }
        }
      }
      steps {
        script {
          def scannerHome = tool 'SonarScanner'
          withSonarQubeEnv("${SONARQUBE_ENV}") {
            withCredentials([string(credentialsId: 'tokensonar', variable: 'SONAR_TOKEN')]) {
              withEnv(["PATH+SONAR=${scannerHome}/bin"]) {
                sh '''
                  set -euo pipefail
                  rm -rf .scannerwork || true

                  RAW="${BRANCH_NAME:-prod}"
                  TARGET_ENV="$RAW"

                  KEY="${PROJECT_NAME}-frontend-${TARGET_ENV}"
                  EXTS=""
                  if [ "$TARGET_ENV" = "prod" ]; then
                    git fetch --tags --force >/dev/null 2>&1 || true
                    VER=$(git describe --tags --always 2>/dev/null || echo "$BUILD_NUMBER")
                    EXTS="$EXTS -Dsonar.projectVersion=${VER}"
                  fi

                  sonar-scanner \
                    -Dsonar.token="$SONAR_TOKEN" \
                    -Dsonar.projectKey="$KEY" \
                    $EXTS
                '''
              }
            }
          }
        }
      }
    }

    stage('Quality Gate - Frontend') {
      when {
        allOf {
          not { changeRequest() }
          expression { fileExists('sonar-project.properties') && fileExists('package.json') }
          expression { ['dev','uat','prod'].contains(env.BRANCH_NAME ?: 'prod') }
        }
      }
      steps {
        timeout(time: 10, unit: 'MINUTES') {
          waitForQualityGate abortPipeline: true
          echo "Quality Gate FRONTEND OK"
        }
      }
    }

    /********************
     * DEPLOYS (NO en PR)
     ********************/
    stage('Deploy DEV') {
      when { allOf { branch 'dev'; not { changeRequest() } } }
      steps {
        sh '''
          set -euxo pipefail
          cd "$WORKSPACE/deploy"
          if docker compose version >/dev/null 2>&1; then CMD="docker compose"; else CMD="docker-compose"; fi
          docker network inspect m2-dev-net >/dev/null 2>&1 || docker network create m2-dev-net
          $CMD -p m2dev -f docker-compose.dev.yml down --remove-orphans || true
          $CMD -p m2dev -f docker-compose.dev.yml build --pull
          $CMD -p m2dev -f docker-compose.dev.yml up -d --force-recreate
          $CMD -p m2dev ps
        '''
      }
    }

    stage('Deploy UAT') {
      when { allOf { branch 'uat'; not { changeRequest() } } }
      steps {
        sh '''
          set -euxo pipefail
          cd "$WORKSPACE/deploy"
          if docker compose version >/dev/null 2>&1; then CMD="docker compose"; else CMD="docker-compose"; fi
          docker network inspect m2-uat-net >/dev/null 2>&1 || docker network create m2-uat-net
          $CMD -p m2uat -f docker-compose.uat.yml down --remove-orphans || true
          $CMD -p m2uat -f docker-compose.uat.yml build --pull
          $CMD -p m2uat -f docker-compose.uat.yml up -d --force-recreate
          $CMD -p m2uat ps
        '''
      }
    }

    stage('Deploy PROD') {
      when { allOf { branch 'prod'; not { changeRequest() } } }
      steps {
        sh '''
          set -euxo pipefail
          cd "$WORKSPACE/deploy"
          if docker compose version >/dev/null 2>&1; then CMD="docker compose"; else CMD="docker-compose"; fi
          docker network inspect m2-prod-net >/dev/null 2>&1 || docker network create m2-prod-net
          $CMD -p m2prod -f docker-compose.prod.yml down --remove-orphans || true
          $CMD -p m2prod -f docker-compose.prod.yml build --pull
          $CMD -p m2prod -f docker-compose.prod.yml up -d --force-recreate
          $CMD -p m2prod ps
        '''
      }
    }
  }

  post {
    failure {
      script {
        try {
          def tz  = TimeZone.getTimeZone('America/Guatemala')
          def now = new Date().format("yyyy-MM-dd HH:mm:ss z", tz)
          def raw       = env.BRANCH_NAME ?: 'prod'
          def targetEnv = raw
          def stageName = (env.STAGE_NAME ?: 'N/A')
          def jobName  = env.JOB_NAME
          def buildNum = env.BUILD_NUMBER
          def duration = (currentBuild.durationString ?: '').replace(' and counting','')
          def nodeName = (env.NODE_NAME ?: 'N/A')
          def buildRoot  = (env.BUILD_URL ?: "").trim()
          if (buildRoot && !buildRoot.endsWith("/")) { buildRoot += "/" }
          def displayURL = (env.RUN_DISPLAY_URL ?: buildRoot)
          def consoleURL = buildRoot + "consoleFull"
          def commit  = sh(script: 'git rev-parse --short HEAD || true', returnStdout: true).trim()
          def author  = sh(script: "git --no-pager show -s --format='%an <%ae>' HEAD || true", returnStdout: true).trim()
          def message = sh(script: "git --no-pager show -s --format='%s' HEAD || true", returnStdout: true).trim()
          def prURL = env.CHANGE_URL ?: '-'
          def prID  = env.CHANGE_ID  ?: '-'

          def subject = "Falló pipeline | ${jobName} #${buildNum} | rama ${raw} (${targetEnv}) | etapa ${stageName}"
          def body = """\
                        Fecha/Hora:   ${now}
                        Resultado:    FAILURE
                        Job:          ${jobName}
                        Build:        #${buildNum}
                        Rama:         ${raw}   (entorno: ${targetEnv})
                        Etapa:        ${stageName}
                        Duración:     ${duration}
                        Nodo:         ${nodeName}

                        Commit:       ${commit}
                        Autor:        ${author}
                        Mensaje:      ${message}

                        Pull Request: ${prURL} (ID: ${prID})
                        Build UI:     ${displayURL}
                        Consola:      ${consoleURL}

                        -- Jenkins auto-notificación
                        """
          mail to: 'hecheverria@unis.edu.gt,jflores@unis.edu.gt', subject: subject, body: body
        } catch (e) {
          echo "No se pudo enviar correo (failure): ${e}"
        }
      }
    }

    unstable {
      script {
        try {
          def tz  = TimeZone.getTimeZone('America/Guatemala')
          def now = new Date().format("yyyy-MM-dd HH:mm:ss z", tz)
          def raw       = env.BRANCH_NAME ?: 'prod'
          def targetEnv = raw
          def stageName = (env.STAGE_NAME ?: 'N/A')
          def jobName  = env.JOB_NAME
          def buildNum = env.BUILD_NUMBER
          def duration = (currentBuild.durationString ?: '').replace(' and counting','')
          def nodeName = (env.NODE_NAME ?: 'N/A')
          def buildRoot  = (env.BUILD_URL ?: "").trim()
          if (buildRoot && !buildRoot.endsWith("/")) { buildRoot += "/" }
          def displayURL = (env.RUN_DISPLAY_URL ?: buildRoot)
          def consoleURL = buildRoot + "consoleFull"
          def commit  = sh(script: 'git rev-parse --short HEAD || true', returnStdout: true).trim()
          def author  = sh(script: "git --no-pager show -s --format='%an <%ae>' HEAD || true", returnStdout: true).trim()
          def message = sh(script: "git --no-pager show -s --format='%s' HEAD || true", returnStdout: true).trim()
          def prURL = env.CHANGE_URL ?: '-'
          def prID  = env.CHANGE_ID  ?: '-'

          def subject = "Pipeline UNSTABLE | ${jobName} #${buildNum} | rama ${raw} (${targetEnv}) | etapa ${stageName}"
          def body = """\
                        Fecha/Hora:   ${now}
                        Resultado:    UNSTABLE
                        Job:          ${jobName}
                        Build:        #${buildNum}
                        Rama:         ${raw}   (entorno: ${targetEnv})
                        Etapa:        ${stageName}
                        Duración:     ${duration}
                        Nodo:         ${nodeName}

                        Commit:       ${commit}
                        Autor:        ${author}
                        Mensaje:      ${message}

                        Pull Request: ${prURL} (ID: ${prID})
                        Build UI:     ${displayURL}
                        Consola:      ${consoleURL}

                        -- Jenkins auto-notificación
                        """
          mail to: 'hecheverria@unis.edu.gt,jflores@unis.edu.gt', subject: subject, body: body
        } catch (e) {
          echo "No se pudo enviar correo (unstable): ${e}"
        }
      }
    }
  }
}
