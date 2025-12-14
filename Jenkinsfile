pipeline {
    agent any
    
    environment {
        APP_NAME = 'spring-app'
        APP_PORT = '8080'
        JAR_PATH = 'build/libs/*.jar'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Cloning repository...'
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building Spring Boot application...'
                sh '''
                    chmod +x gradlew
                    ./gradlew clean build -x test
                '''
            }
        }
        
        stage('Stop Old Application') {
            steps {
                echo 'Stopping old application...'
                sh '''
                    PID=$(ps aux | grep 'java.*spring-app' | grep -v grep | awk '{print $2}')
                    if [ ! -z "$PID" ]; then
                        echo "Killing process $PID"
                        kill -9 $PID
                        sleep 3
                    else
                        echo "No running application found"
                    fi
                '''
            }
        }
        
        stage('Deploy') {
            steps {
                echo 'Deploying new application...'
                sh '''
                    # Find JAR file
                    JAR_FILE=$(find build/libs -name "*.jar" -not -name "*plain*" | head -1)
                    echo "Deploying: $JAR_FILE"
                    
                    # Run in background
                    nohup java -Xmx256m -jar $JAR_FILE > /var/log/spring-app.log 2>&1 &
                    
                    # Wait and check
                    sleep 10
                    
                    # Verify it's running
                    if ps aux | grep 'java.*spring-app' | grep -v grep > /dev/null; then
                        echo "Application started successfully"
                    else
                        echo "Failed to start application"
                        exit 1
                    fi
                '''
            }
        }
        
        stage('Health Check') {
            steps {
                echo 'Checking application health...'
                sh '''
                    for i in {1..10}; do
                        if curl -f http://localhost:8080/actuator/health || curl -f http://localhost:8080; then
                            echo "Application is healthy"
                            exit 0
                        fi
                        echo "Attempt $i failed, waiting..."
                        sleep 5
                    done
                    echo "Health check failed"
                    exit 1
                '''
            }
        }
    }
    
    post {
        success {
            echo 'Deployment successful!'
        }
        failure {
            echo 'Deployment failed!'
        }
        always {
            echo 'Cleaning up workspace...'
            cleanWs()
        }
    }
}
