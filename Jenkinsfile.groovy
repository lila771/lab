#!/usr/bin/env groovy

pipeline {
    agent {
	    docker { 
		    reuseNode true
		    image 'maven:3.5.2-jdk-8-alpine' 
		}
	}
	environment {
	   IMAGE = readMavenPom().getArtifactId()
           VERSION = readMavenPom().getVersion()
    }
	
    stages {
	    stage('Build') {
            steps {
                withMaven(options: [findbugsPublisher(), junitPublisher(ignoreAttachments: false)]) {
                sh 'mvn clean findbugs:findbugs package'
			    }
            }
		}
    }		
	post {
        success {
            archiveArtifacts(artifacts: '**/target/*.jar', allowEmptyArchive: true)
        }
    }
}
