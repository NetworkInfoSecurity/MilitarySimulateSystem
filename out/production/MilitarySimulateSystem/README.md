# MilitarySimulateSystem
The second project of Network Information Security course

- Development environment
  - IntelliJ IDEA
  - JDK 1.8
  - JavaFx 8

- Source Structure  
  - model（基础模型）
    - Soldier.java: 士兵  
    - Target.java: 作战目标（如箱子和信件）
  - view（视图）
    - MainView: 主视图  
  - util（算法工具类）  
    - RSA.java: RSA认证算法  
        - 加密字符串
        - 解密字符串
        - 验证士兵合法性
        - 生成秘钥
    - Millionaire.java: 百万富翁算法  
    - ElectronicVote.java: 电子投票算法  
    - SharedKey.java: 共享秘钥算法  
        - 获得分享秘钥
    - xmlReader.java: 文件读取接口
        - 从xml文件读取士兵数据
  - controller（逻辑控制）  
    - SceneCreator.java: 创建场景、初始化、模拟演习、调用算法等  
  - MainApp.java: 主函数、程序启动  