@echo off
echo 这是一个自动git项目的脚本
pause
cd..
echo 正在删除java-web..
rmdir /s /q java-web
echo 正在 clone 项目
git clone http://cloud.yuanzhangzcc.com:3000/world/java-web.git
start cmd java-web