# Springboard

### **프로젝트 기본 사양**

- 게시판
- 기본 포스팅 CRUD
- 포스팅마다 댓글
- 회원 개념 존재

### **사용 기술**

- 프론트엔드: React + TypeScript + Vite
- 백엔드: Spring boot + Java
- 데이터베이스: Mysql(Docker image)
	- ORM: 차후 도입 예정
- 컨테이너: docker, docker-compose 
- IDE: IntelliJ(Backend), VSCode(Frontend)
- LLM: Codex

### **컨테이너 구조**

- compose
	- frontend -> 5173
	- backend -> 8080
	- db 
    
### **스키마 종류**

- users
- posts
- comments