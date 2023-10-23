pipeline {
    agent { label 'Retailpim_build_server' }
    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "maven_3.8.3"
    }
    environment {
        DEFAULT_BRANCH = 'master'
    }
    stages {
        stage('git') {
            steps {
                script {
                    env.BRANCH_TO_BUILD = DEFAULT_BRANCH
                }
                // Get the code from the 'main' branch of a GitHub repository
                git url: 'https://gitlab.com/sales_retail/jwt-utils.git', branch: "${env.BRANCH_TO_BUILD}", credentialsId: 'GitLab-Cred-Saikrishna'
            }          
        }
        stage('Which Java?') {
            steps {
                sh 'java --version'
            }
        }
        stage('Build parent pom') {
            steps {
                // Run Maven on a Unix agent.
                sh 'mvn release:clean -B release:prepare release:perform'
            }
        }
        stage('Run Shell Script') {
            steps {
                // to copy retailinsights-parent-pom folder to GatewayAPI
                sh 'chmod +x script.sh'
                sh './script.sh'
            }
        }
        
    }
}
