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
     * STATIC CHECKS (solo en PR)
     ********************/
    stage('Static Checks') {
      when { changeRequest() }
      steps {
        script {
          echo "Ejecutando validaciones estáticas para PR..."
          // Backend: compila sin tests
          dir('backend') {
            sh 'mvn -q verify -DskipTests'
          }
          // Frontend: lint si existe script "lint"
          nodejs('Node 20') {
            sh '''
              if [ -f package.json ] && grep -q '"lint"' package.json; then
                echo "Instalando deps front para lint..."
                npm ci --no-audit --no-fund
                echo "Ejecutando lint del frontend..."
                npm run lint || { echo "Lint falló"; exit 1; }
              else
                echo "No hay script lint en package.json, se omite"
              fi
            '''
          }
        }
      }
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

    /********************
     * SONAR - BACKEND (usa backend/sonar-project.properties)
     ********************/
    stage('SonarQube Analysis - Backend (con cobertura)') {
      when {
        anyOf {
          changeRequest() // PR
          expression { ['dev','uat','prod','main','master'].contains(env.BRANCH_NAME ?: 'prod') } // ramas reales
        }
      }
      steps {
        script {
          def scannerHome = tool 'SonarScanner'
          withSonarQubeEnv("${SONARQUBE_ENV}") {
            withCredentials([string(credentialsId: 'tokensonar', variable: 'SONAR_TOKEN')]) {
              withEnv(["PATH+SONAR=${scannerHome}/bin"]) {
                dir('backend') {
                  sh 'rm -rf .scannerwork || true'
                  sh '''
                    EXTS=""
                    if [ -n "${CHANGE_ID}" ]; then
                      # Modo PR
                      EXTS="$EXTS -Dsonar.pullrequest.key=${CHANGE_ID} -Dsonar.pullrequest.branch=${CHANGE_BRANCH} -Dsonar.pullrequest.base=${CHANGE_TARGET}"
                      [ -n "${GIT_COMMIT}" ] && EXTS="$EXTS -Dsonar.scm.revision=${GIT_COMMIT}"
                    else
                      # Modo rama
                      EXTS="$EXTS -Dsonar.branch.name=${BRANCH_NAME}"
                      if [ "${BRANCH_NAME}" = "main" ] || [ "${BRANCH_NAME}" = "master" ] || [ "${BRANCH_NAME}" = "prod" ]; then
                        git fetch --tags --force >/dev/null 2>&1 || true
                        VER=$(git describe --tags --always 2>/dev/null || echo "$BUILD_NUMBER")
                        EXTS="$EXTS -Dsonar.projectVersion=${VER}"
                      fi
                    fi

                    # Usa backend/sonar-project.properties por defecto (está en este directorio)
                    sonar-scanner -Dsonar.token=$SONAR_TOKEN $EXTS
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
        anyOf {
          changeRequest()
          expression { ['dev','uat','prod','main','master'].contains(env.BRANCH_NAME ?: 'prod') }
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

    /********************
     * SONAR - FRONTEND (usa root/sonar-project.properties)
     ********************/
    stage('SonarQube Analysis - Frontend (sin cobertura)') {
      when {
        allOf {
          expression { fileExists('sonar-project.properties') && fileExists('package.json') }
          anyOf {
            changeRequest()
            expression { ['dev','uat','prod','main','master'].contains(env.BRANCH_NAME ?: 'prod') }
          }
        }
      }
      steps {
        script {
          def scannerHome = tool 'SonarScanner'
          withSonarQubeEnv("${SONARQUBE_ENV}") {
            withCredentials([string(credentialsId: 'tokensonar', variable: 'SONAR_TOKEN')]) {
              withEnv(["PATH+SONAR=${scannerHome}/bin"]) {
                sh 'rm -rf .scannerwork || true'
                sh '''
                  EXTS=""
                  if [ -n "${CHANGE_ID}" ]; then
                    # Modo PR
                    EXTS="$EXTS -Dsonar.pullrequest.key=${CHANGE_ID} -Dsonar.pullrequest.branch=${CHANGE_BRANCH} -Dsonar.pullrequest.base=${CHANGE_TARGET}"
                    [ -n "${GIT_COMMIT}" ] && EXTS="$EXTS -Dsonar.scm.revision=${GIT_COMMIT}"
                  else
                    # Modo rama
                    EXTS="$EXTS -Dsonar.branch.name=${BRANCH_NAME}"
                    if [ "${BRANCH_NAME}" = "main" ] || [ "${BRANCH_NAME}" = "master" ] || [ "${BRANCH_NAME}" = "prod" ]; then
                      git fetch --tags --force >/dev/null 2>&1 || true
                      VER=$(git describe --tags --always 2>/dev/null || echo "$BUILD_NUMBER")
                      EXTS="$EXTS -Dsonar.projectVersion=${VER}"
                    fi
                  fi

                  # Usa sonar-project.properties de la raíz del repo
                  sonar-scanner -Dsonar.token=$SONAR_TOKEN $EXTS
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
          expression { fileExists('sonar-project.properties') && fileExists('package.json') }
          anyOf {
            changeRequest()
            expression { ['dev','uat','prod','main','master'].contains(env.BRANCH_NAME ?: 'prod') }
          }
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
      when {
        allOf {
          branch 'dev'
          not { changeRequest() }
        }
      }
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
      when {
        allOf {
          branch 'uat'
          not { changeRequest() }
        }
      }
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
      when {
        allOf {
          anyOf { branch 'prod'; branch 'main'; branch 'master' }
          not { changeRequest() }
        }
      }
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
          def targetEnv = (['main'].contains(raw)) ? 'prod' : raw
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
          def targetEnv = (['main','master'].contains(raw)) ? 'prod' : raw
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
