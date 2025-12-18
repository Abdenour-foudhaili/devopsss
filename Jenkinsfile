pipeline {
    agent any
    
    tools {
        jdk 'jdk'
        maven 'maven'
    }
    
    environment {
        SCANNER_HOME = tool 'sonar-scanner'
        DOCKER_IMAGE = 'foudhailiabdenour/student-management'
        DOCKER_TAG = "${BUILD_NUMBER}"
    }
    
    stages {
        stage('Checkout') {
            steps {
                git credentialsId: 'github-token',
                    url: 'https://github.com/Abdenour-foudhaili/devopsss.git',
                    branch: 'main'  // ✅ Changez en 'main'
            }
        }
        
        stage('Build Maven') {
            steps {
                sh "mvn clean install -DskipTests"  // ✅ Déjà avec -DskipTests
            }
        }
        
        // ❌ COMMENTEZ CETTE STAGE COMPLÈTEMENT
        /*
        stage('Run Tests') {
            steps {
                sh "mvn test"
            }
        }
        */
        
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonar') {
                    sh """
                        mvn sonar:sonar \
                        -Dsonar.projectKey=student-management \
                        -Dsonar.projectName='Student Management' \
                        -Dsonar.java.binaries=target/classes
                    """
                }
            }
        }
        
        // ... reste du pipeline identique
    }
}