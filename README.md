## 📬 API de Envio de E-mails

### ✅ Retorno de status HTTP com mensagens claras

---

## 📡 Endpoints

### Enviar E-mail

`POST /email/send`

**Request Body (JSON):**

```json
{
  "to": ["destinatario1@gmail.com", "destinatario2@gmail.com"],
  "type": "WELCOME",
  "body": {
    "nome": "Lucas",
    "code": "456789",
    "magicLink": "https://exemplo.com/redefinir"
  },
  "date": "2025-07-05T15:00:00"
}
```

**Campos obrigatórios:**

- `to`: Lista de destinatários
- `type`: Tipo do e-mail (deve existir no enum `Type`)
- `body`: Objeto com chaves que correspondem aos `{{placeholders}}` no HTML
- `date`: (opcional) Data de envio (padrão: agora)

**Resposta:**

- `200 OK` – E-mail enviado com sucesso
- `500 Internal Server Error` – Falha ao enviar o e-mail (mensagem de erro no corpo)

---

## ⚙️ Configuração

No arquivo `application.properties`, configure suas credenciais de e-mail:

```properties
email.username=seu-email@gmail.com
email.password=sua-senha-ou-token-de-aplicacao
email.debug=true
```

> 🔐 Recomenda-se usar **token de aplicativo** ao invés da senha real no Gmail.

---

## 🚀 Como rodar

1. Configure o `application.properties`
2. Compile o projeto:
   ```bash
   mvn clean install
   ```
3. Execute a aplicação Spring Boot normalmente
4. Use o endpoint `/email/send` para enviar e-mails com templates dinâmicos

---

## 📁 Templates HTML

Os arquivos `.compiled.html` devem estar na pasta:

```
src/main/resources/emails/
```

Exemplos:

- `welcome.compiled.html` (type: `WELCOME`)
- `reset_password.compiled.html` (type: `RESET_PASSWORD`)
- `delete_account.compiled.html` (type: `DELETE_ACCOUNT`)

Esses arquivos devem conter placeholders como `{{nome}}`, `{{code}}`, `{{magicLink}}`, etc., que serão substituídos dinamicamente.

---

## 🧩 Adicionando Novos Templates

1. Crie um novo HTML em `src/main/resources/emails`
2. Compile o template com o [Bootstrap Email](https://bootstrapemail.com/) (CLI ou web)
3. Adicione o `enum` correspondente em:

```java
public enum Type {
  WELCOME,
  RESET_PASSWORD,
  DELETE_ACCOUNT
}
```

4. Use o novo `type` no corpo da requisição `POST /email/send`

---