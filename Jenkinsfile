pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh './clank build'
            }
        }

        stage('Artifact') {
            steps {
                archiveArtifacts 'clank.jar'
            }
        }
    }
}
