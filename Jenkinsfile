pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh './build.sh'
            }
        }

        stage('Artifact') {
            steps {
                archiveArtifacts 'clank.jar'
            }
        }
    }
}
