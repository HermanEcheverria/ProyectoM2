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
            echo "No hay package.json en la raíz; salto instalación de front."
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
        
          sh 'mvn -q dependency:copy-dependencies -DoutputDirectory=target/dependency'
          
          sh 'mvn -q jacoco:report'
        
          sh '''
            ls -lah target/site/jacoco || true
            test -f target/site/jacoco/jacoco.xml || {
              echo "No se encontró backend/target/site/jacoco/jacoco.xml. Revisa la config de JaCoCo.";
              exit 1;
            }
          '''
        }
      }
    }

    stage('SonarQube Analysis (repo completo)') {
      steps {
        script {
          def scannerHome = tool 'SonarScanner'  
          withSonarQubeEnv("${SONARQUBE_ENV}") {
            withCredentials([string(credentialsId: 'tokensonar', variable: 'SONAR_TOKEN')]) {
            
              sh '''
                export PATH="''' + scannerHome + '''/bin:$PATH"
                sonar-scanner -Dsonar.token=$SONAR_TOKEN
              '''
            }
          }
        }
      }
    }

    stage('Quality Gate') {
      steps {
        timeout(time: 10, unit: 'MINUTES') {
          waitForQualityGate abortPipeline: true
          echo "SonarQube OK (Quality Gate) para ${env.BRANCH_NAME}"
        }
      }
    }
  }

  post {
  failure {
    mail to: 'hecheverria@unis.edu.gt',
         subject: "Falló pipeline en rama ${env.BRANCH_NAME}",
         body: "El pipeline falló en la etapa ${env.STAGE_NAME}. Revisar Jenkins."
  }
  unstable {
    mail to: 'hecheverria@unis.edu.gt',
         subject: "Pipeline UNSTABLE en ${env.BRANCH_NAME}",
         body: "El pipeline quedó UNSTABLE en la etapa ${env.STAGE_NAME}. Revisar Jenkins."
  }
}

