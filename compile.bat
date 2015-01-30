@echo off
@title CLCompiler ~ Building
mvn clean install package
@title CLCompiler ~ Finishing
explorer target