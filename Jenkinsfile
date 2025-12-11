pipeline {
    agent any

    tools {
        maven 'maven'  
        jdk 'jdk17'    
    }

    environment {
        GITHUB_TOKEN = credentials('github-token')
        SONAR_TOKEN  = credentials('jenkins')
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'master',
                    url: 'https://github.com/Abdenour-foudhaili/devopsss.git'
            }
        }

        stage('Build Maven') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonar') {
                    sh '''
                    mvn sonar:sonar \
                    -Dsonar.projectKey=devopsss \
                    -Dsonar.host.url=http://172.19.187.155:9000 \
                    -Dsonar.login=$SONAR_TOKEN
                    '''
                }
            }
        }

        stage("Quality Gate") {
            steps {
                timeout(time: 1, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Docker build & push') {
            steps {
                sh '''
                docker build -t devopsss .
                '''
            }
        }
    }
}
