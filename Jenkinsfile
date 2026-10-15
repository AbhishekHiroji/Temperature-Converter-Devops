
pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'abhishekhiroji/temperature-converter'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean compile'
            }
        }

        stage('Selenium Tests') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Package') {
            steps {
                bat 'mvn package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat 'docker build -t %DOCKER_IMAGE%:%BUILD_NUMBER% -t %DOCKER_IMAGE%:latest .'
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKERHUB_USERNAME',
                        passwordVariable: 'DOCKERHUB_TOKEN'
                    )
                ]) {
                    bat '''
                        @echo off
                        echo %DOCKERHUB_TOKEN%| docker login --username %DOCKERHUB_USERNAME% --password-stdin
                        if errorlevel 1 exit /b 1
                        docker push %DOCKER_IMAGE%:%BUILD_NUMBER%
                        if errorlevel 1 exit /b 1
                        docker push %DOCKER_IMAGE%:latest
                        if errorlevel 1 exit /b 1
                        docker logout
                    '''
                }
            }
        }
    }

    post {
        success {
            echo 'Build, Selenium tests, Docker build and Docker Hub push completed!'
        }
        failure {
            echo 'Pipeline failed. Check the Console Output.'
        }
    }
}
