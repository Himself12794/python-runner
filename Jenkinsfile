pipeline {
	agent any
	tools {
		maven 'Maven-3.3.1'
		jdk 'JDK1.8.0'
	}
    
	environment {
		CDA_SPARK_ROOM = "15531750-1395-11e5-b45d-dd155cb7d786"
	}
	
    stages {
		
        stage ('Build') {
        	// Clean, build, and run tests
            steps {
				notifyBuildStart()
                sh 'mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install' 
            }
            // Make test results visible in Jenkins UI if the install step completed successfully
            post {
                success {
                    junit testResults: 'target/surefire-reports/**/*.xml', allowEmptyResults: true 
                }
            }
        }

        stage ('Quality Assurance') {
        
        	// Run these stages in parallel
        	parallel {
					
				// Run sonar scanner, and report to CDA
		        stage ('SonarQube Quality Scan') {
		        	steps {
						// Automatically detect maven project, no further config necessary
						sonarScan('Sonar')
		        	}
		        }
		        
		        // Upload the artifacts to artifactory
				stage ('Deploy Artifacts') {
					steps {
				        mavenArtifactoryDeploy() {
				        	deployerReleaseRepo 'ext-release-local'
				        	deployerSnapshotRepo 'ext-snapshot-local'
				        }
					}
				}
	        }
        }
		
    }
	post {
		always {
			notifyBuildEnd()
		}
	}
}
