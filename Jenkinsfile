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
    }
}
