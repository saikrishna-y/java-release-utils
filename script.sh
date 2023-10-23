#!/bin/bash
DIRi=/var/www/html/RI_Common
DIRt=/var/www/html/RI_Common/jwt-utils/target
sudo cp -r /home/jenkins/workspace/RI_Common/jwt-utils $DIRi
sudo chmod -R 777 $DIRt
sudo chown -R vijaya:vijaya $DIRt