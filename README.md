# forum - 技术论坛后端系统

基于 Spring Boot 3 + MyBatis-Plus + MySQL 的论坛后端系统，暑期实习备战项目，持续开发中。

## 技术栈

- Java 17 + Spring Boot 3.4.5
- MyBatis-Plus 3.5.10
- MySQL 8.0
- Maven

## 已完成功能

- [x] 用户查询接口：GET /api/user/{id}
- [x] 用户注册接口：POST /api/user/register

## 开发计划

- [ ] 用户登录（JWT）
- [ ] 帖子 CRUD
- [ ] 评论与通知
- [ ] 点赞与热榜（Redis）

## 本地运行

1. 环境：JDK 17+、Maven 3.6+、MySQL 8.0
2. 建库建表：执行 `sql/init.sql`
3. 修改 `src/main/resources/application.yml` 中的数据库密码
4. 启动：`mvn spring-boot:run`
5. 验证：浏览器访问 http://localhost:8080/api/user/1

## 接口文档

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/user/{id} | 按 ID 查询用户 |
| POST | /api/user/register | 用户注册 |