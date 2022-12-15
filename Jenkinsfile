pipeline {
    agent any

    tools {
        gradle "gradle7"
    }

    options {
        timeout(time: 10, unit: 'MINUTES')
        timestamps()
    }

    triggers { pollSCM('*/1 * * * *') }

    environment {
        DOCKER_IMAGE = "venu/helloworld-gradle"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/develop']], extensions: [], userRemoteConfigs: [[credentialsId: 'GitHub_token', url: 'https://github.com/vgtstl/java-hello-world-with-gradle.git']]])
               }
        }
        stage('Version') {
            steps {
                sh 'gradle --version'
            }
        }
        stage('Build') {
            steps {
                sh 'gradle clean build'
            }
            post {
                always {
                    archiveArtifacts artifacts: 'build/libs/*.jar', followSymlinks: false
                }
            }
        }
        stage('DockerBuild'){
            steps {
                sh "docker build -t ${env.DOCKER_IMAGE} ."
            }
        }
        stage('DockerScan'){
            steps {
                echo "docker scan ${env.DOCKER_IMAGE}"
            }
        }
        stage('DockePush'){
             steps {
                echo "docker  push ${env.DOCKER_IMAGE}"
             }
        }
        stage('Deploy to Dev'){
             steps {
                echo 'Dev Deployment Completed'
             }
        }
        stage('Deploy to QA'){
             steps {
                echo 'QA Deployment Completed'
             }
        }
        stage('Deploy to Prod'){
            steps {
                input('Do you wants to proceed with prod deployment')
                echo 'Prod Deployment Completed'
            }
        }
    }
    
}
