# 苍穹外卖系统（Sky Take Out）

> 基于 Spring Boot + MyBatis 的餐饮外卖平台后端系统，包含管理端与用户端（微信小程序）双端功能。

## 项目简介

苍穹外卖是针对餐饮企业推出的一套外卖业务的管理系统。项目采用前后端分离架构，本仓库为**后端服务**部分，提供管理端后台 API 和用户端小程序 API，支持从店铺营业、菜品管理到用户下单、微信支付、订单催单的完整外卖业务闭环。

## 技术栈

| 分类 | 技术 | 版本 |
|:---|:---|:---:|
| 核心框架 | Spring Boot | 2.7.3 |
| ORM 框架 | MyBatis | 2.2.0 |
| 数据库 | MySQL | 8.0+ |
| 缓存 | Redis | - |
| 数据库连接池 | Druid | 1.2.1 |
| 分页插件 | PageHelper | 1.3.0 |
| 身份认证 | JWT (jjwt) | 0.9.1 |
| 接口文档 | Knife4j (Swagger) | 3.0.2 |
| 文件存储 | 阿里云 OSS | 3.10.2 |
| 支付 | 微信支付 API v3 | 0.4.8 |
| Excel 导出 | Apache POI | 3.16 |
| JSON 处理 | Fastjson | 1.2.76 |
| 简化代码 | Lombok | 1.18.20 |

## 项目结构

```
sky-take-out
├── sky-common          # 通用模块
│   ├── constant        # 常量定义
│   ├── context         # 线程上下文（BaseContext）
│   ├── exception       # 全局异常处理
│   ├── json            # JSON 处理器
│   ├── properties      # 配置属性类
│   ├── result          # 统一返回结果（Result、PageResult）
│   └── utils           # 工具类（JWT、文件、日期等）
│
├── sky-pojo            # 实体模块
│   ├── entity          # 数据库实体类
│   ├── dto             # 数据传输对象
│   └── vo              # 视图对象
│
└── sky-server          # 服务模块（核心）
    └── src/main/java/com/sky
        ├── controller
        │   ├── admin/         # 管理端接口
        │   ├── user/          # 用户端接口
        │   └── notify/        # 支付回调接口
        ├── service            # 业务逻辑层
        ├── mapper             # 数据访问层
        ├── config             # 配置类
        ├── interceptor        # 拦截器（JWT 校验）
        ├── aspect             # AOP 切面（自动填充、日志）
        └── SkyApplication     # 启动类
```

## 功能模块

### 管理端

| 模块 | 接口 | 说明 |
|:---|:---|:---|
| 员工管理 | `EmployeeController` | 员工登录/退出、新增、修改、分页查询、启用禁用 |
| 分类管理 | `CategoryController` | 分类新增、修改、删除、分页查询（菜品/套餐分类） |
| 菜品管理 | `DishController` | 菜品新增、修改、删除、分页查询、起售/停售、按分类查询 |
| 套餐管理 | `SetmealController` | 套餐新增、修改、删除、分页查询、起售/停售 |
| 店铺管理 | `ShopController` | 营业状态设置与查询 |
| 通用功能 | `CommonController` | 文件上传（阿里云 OSS） |

### 用户端（微信小程序）

| 模块 | 接口 | 说明 |
|:---|:---|:---|
| 用户登录 | `UserController` | 微信小程序授权登录（JWT 签发） |
| 地址簿 | `AddressBookController` | 收货地址增删改查、设置默认地址 |
| 商品浏览 | `CategoryController` / `DishController` / `SetmealController` | 分类列表、菜品列表、套餐列表 |
| 购物车 | `ShoppingCartController` | 加入购物车、查看购物车、清空购物车 |
| 订单管理 | `OrderController` | 提交订单、历史订单、订单详情、催单、再来一单 |
| 支付回调 | `PayNotifyController` | 微信支付结果异步通知（WebSocket 推送） |
| 店铺状态 | `ShopController` | 查询店铺营业状态 |

## 快速开始

### 环境要求

- JDK 1.8+
- MySQL 8.0+
- Redis
- Maven 3.6+
- 微信开发者工具（用户端调试）

### 1. 克隆项目

```bash
git clone https://github.com/mx111-jiayou/sky--take--out.git
cd sky--take--out
```

### 2. 配置数据库

创建数据库 `sky_take_out` 并导入初始 SQL 脚本：

```sql
CREATE DATABASE sky_take_out DEFAULT CHARACTER SET utf8mb4;
USE sky_take_out;
-- 导入项目提供的 SQL 文件
```

### 3. 修改配置文件

编辑 `sky-server/src/main/resources/application-dev.yml`，填入你的环境配置：

```yaml
sky:
  datasource:
    host: localhost
    port: 3306
    database: sky_take_out
    username: root
    password: 你的数据库密码
  redis:
    host: localhost
    port: 6379
    password:        # 无密码可留空
    database: 10
  alioss:
    endpoint: your-endpoint          # 阿里云 OSS
    access-key-id: your-access-key-id
    access-key-secret: your-access-key-secret
    bucket-name: your-bucket-name
  wechat:
    app-id: your-wechat-appid        # 微信小程序 AppID
    secret: your-wechat-secret       # 微信小程序 Secret
```

### 4. 构建与启动

```bash
# 编译打包
mvn clean install -DskipTests

# 启动服务
cd sky-server
mvn spring-boot:run
```

服务启动后默认监听 **8080** 端口。

### 5. 查看接口文档

启动成功后访问 Knife4j 接口文档：

```
http://localhost:8080/doc.html
```

## 核心设计

### 双端 JWT 认证

系统为管理端和用户端设计了独立的 JWT 认证体系：

- **管理端**：密钥 `admin-secret-key`，Token 字段名 `token`，有效期 2 小时
- **用户端**：密钥 `user-secret-key`，Token 字段名 `authentication`，有效期 2 小时

通过 `JwtTokenAdminInterceptor` 和 `JwtTokenUserInterceptor` 两个拦截器分别校验。

### 自动填充字段

使用 AOP 切面 `AutoFillAspect` 自动填充 `create_time`、`update_time`、`create_user`、`update_user` 四个公共字段，减少业务代码重复。

### ThreadLocal 上下文

通过 `BaseContext` 基于 ThreadLocal 存储当前登录用户 ID，在自动填充和业务逻辑中直接获取，避免层层传参。

### 缓存优化

对高频读取的数据（如菜品列表、套餐数据）使用 Redis + Spring Cache 进行缓存，降低数据库压力，提升响应速度。

### WebSocket 消息推送

支付成功后通过 WebSocket 向管理端浏览器推送新订单提醒，实现实时订单通知；用户催单时同样通过 WebSocket 通知商家。

## 业务流程

```
用户端下单流程：
微信登录 → 浏览菜品 → 加入购物车 → 选择地址 → 提交订单
    → 微信支付 → 支付回调 → WebSocket 通知商家 → 商家接单
    → 用户催单 → WebSocket 提醒商家 → 订单完成

管理端管理流程：
员工登录 → 设置营业状态 → 管理分类 → 管理菜品 → 管理套餐
    → 接收订单（WebSocket） → 接单/拒单 → 订单完成
```

## 开发规范

- **分层架构**：Controller → Service → Mapper，职责清晰
- **DTO/VO 分离**：入参用 DTO，出参用 VO，实体类不直接暴露
- **统一返回**：所有接口返回 `Result<T>` 统一格式
- **全局异常**：`GlobalExceptionHandler` 捕获异常并转为友好提示
- **分页规范**：使用 PageHelper 统一分页，返回 `PageResult`

## 接口示例

### 管理端 - 员工登录

```
POST /admin/employee/login
Content-Type: application/json

{
  "username": "admin",
  "password": "123456"
}
```

### 用户端 - 微信登录

```
POST /user/user/login
Content-Type: application/json

{
  "code": "微信登录授权码"
}
```

### 用户端 - 提交订单

```
POST /user/order/submit
Header: authentication: {用户JWT令牌}
Content-Type: application/json

{
  "addressBookId": 1,
  "payMethod": 1,
  "remark": "少放辣"
}
```

## 版本记录

| 日期 | 版本 | 说明 |
|:---|:---|:---|
| 2026-04-27 | v1.0 | 项目初始化，搭建基础框架 |
| 2026-04-28 | v1.1 | 员工管理模块（登录、新增） |
| 2026-04-29 | v1.2 | 员工分页查询、分类管理 |
| 2026-05-06 | v1.3 | 菜品管理模块 |
| 2026-05-14 | v1.4 | 客户催单业务、微信支付对接 |

## License

本项目仅用于学习交流目的。
