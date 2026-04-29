# 📚 Sistema Escolar

Sistema de gerenciamento escolar desenvolvido em **Java**, voltado para o cadastro de alunos, criação de turmas e matrícula de estudantes conforme a faixa etária de cada etapa de ensino.

---

## 🚀 Funcionalidades

- ✅ Cadastro de alunos  
- ✅ Validação automática de CPF  
- ✅ Cálculo automático da idade  
- ✅ Cadastro de turmas  
- ✅ Matrícula conforme faixa etária  
- ✅ Listagem de alunos e turmas  
- ✅ Controle de duplicidade de registros  

---

## 🧩 Estrutura do Projeto

### 👨‍🎓 Aluno

Representa um aluno com as seguintes informações:

- Nome  
- CPF  
- Endereço  
- Data de nascimento  
- Idade calculada automaticamente  

---

### 📋 ListaDeAlunos

Classe responsável por:

- Inserção de alunos  
- Busca por CPF  
- Ordenação de registros  
- Remoção de alunos  
- Controle de duplicidade  

---

### 🏫 Turma

Representa uma turma com:

- Código identificador  
- Etapa de ensino  
- Ano letivo  
- Limite de vagas  
- Lista de alunos matriculados  

---

### ⚠️ ExcecaoDeAlunoJaExistente

Exceção personalizada criada para impedir o cadastro de alunos duplicados no sistema.

---

## 🎯 Regras de Matrícula por Idade

| Etapa de Ensino                     | Faixa Etária |
|------------------------------------|-------------|
| Infantil                           | 0 a 5 anos |
| Fundamental Anos Iniciais          | 6 a 10 anos |
| Fundamental Anos Finais            | 11 a 14 anos |
| Ensino Médio                       | 15 a 18 anos |

---

## ▶️ Como Executar

### 1️⃣ Compilar o projeto

```bash
javac SistemaEscolar.java

### 1️⃣ Executar o sistema

```bash
java SistemaEscolar