jruby(snoar)
---------------

2010.10.11
----------

   1. 修改 app.jruby/pom.xml,直接mvn install -Dmaven.test.skip=true

      subprojects/app-update-center mvn install
      subprojects/app-update-center/app-update-center-common mvn install
      app-server mvn eclipse:eclipse

   2、增加git仓库 http://github.com/yangjiandong/appjruby
   git remote add origin git@github.com:yangjiandong/appjruby.git

   git push origin master:refs/heads/master

   $ ssh-keygen
    (ssh-keygen -C "你的email地址" -t rsa)
    Generating public/private rsa key pair.
    Enter file in which to save the key (/Users/schacon/.ssh/id_rsa):
    Enter passphrase (empty for no passphrase):
    Enter same passphrase again:
    Your identification has been saved in /Users/schacon/.ssh/id_rsa.
    Your public key has been saved in /Users/schacon/.ssh/id_rsa.pub.
    The key fingerprint is:
    43:c5:5b:5f:b1:f1:50:43:ad:20:a6:92:6a:1f:9a:3a schacon@agadorlaptop.local

   提交时，需将ssh-key 加到 github

   github user:yangjiandong,123456789,young.jiandong@gmail.com

   2、建立branch 3.3.2
   git branch 3.3.2
   git push origin 3.3.2

   3、手工建立eclipse项目

   a、建立m2_home变量
     mvn -Declipse.workspace=<path-to-eclipse-workspace> eclipse:add-maven-repo
   b、生成eclipse项目
     mvn eclipse:eclipse
     bin/eclipse.bat

   4、clone

   git clone git://github.com/yangjiandong/sshapp.git sshapp

2010.10.10
-----------

   1. 基本搭建完sill-server
      
2010.10.09
-----------

mvn:
   base mvn install
   app-check-api
   app-channel
   app-colorizer
   app-graph
   app-squid
   app-duplications
   app-plugin-api
   app-core
   app-deprecated >mvn install -Dmaven.test.skip=true
   app-testing-harness
   app-server