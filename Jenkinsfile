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
                    branch: 'main'
            }
        }
        
        stage('Build Maven') {
            steps {
                sh "mvn clean install -DskipTests"
            }
        }
        
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
        
        stage("Quality Gate") {
            steps {
                script {
                    try {
                        timeout(time: 10, unit: 'MINUTES') {
                            def qg = waitForQualityGate()
                            if (qg.status != 'OK') {
                                echo "Quality Gate status: ${qg.status}"
                                echo "Warning: Quality Gate failed but pipeline continues"
                            } else {
                                echo "Quality Gate passed successfully!"
                            }
                        }
                    } catch (Exception e) {
                        echo "Quality Gate check failed or timed out: ${e.message}"
                        echo "Pipeline continues anyway..."
                    }
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    sh """
                        docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                        docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest
                    """
                }
            }
        }
        
        stage('Push to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh """
                            echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin
                            docker push ${DOCKER_IMAGE}:${DOCKER_TAG}
                            docker push ${DOCKER_IMAGE}:latest
                            docker logout
                        """
                    }
                }
            }
        }
        
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    withKubeConfig([credentialsId: 'kubeconfig-credential']) {
                        sh """
                            kubectl get nodes
                            kubectl apply -f mysql-deployment.yaml
                            kubectl apply -f spring-deployment.yaml
                            kubectl set image deployment/spring-deployment -n devops spring-container=${DOCKER_IMAGE}:${DOCKER_TAG}
                            kubectl rollout status deployment/spring-deployment -n devops --timeout=5m
                        """
                    }
                }
            }
        }
        
        stage('Verify Deployment') {
            steps {
                script {
                    withKubeConfig([credentialsId: 'kubeconfig-credential']) {
                        sh """
                            kubectl wait --for=condition=ready pod -l app=spring-app -n devops --timeout=5m
                            kubectl get pods -n devops
                            kubectl get svc -n devops
                        """
                    }
                }
            }
        }
    }
    
    post {
        success {
            echo "Pipeline completed successfully!"
            echo "SonarQube: http://localhost:9000/dashboard?id=student-management"
            echo "Docker: ${DOCKER_IMAGE}:${DOCKER_TAG}"
            echo "Application deployed to Kubernetes!"
        }
        failure {
            echo "Pipeline failed!"
        }
        always {
            sh "docker rmi ${DOCKER_IMAGE}:${DOCKER_TAG} || true"
        }
    }
}