/*---------------------------------------------------------------------------*\
# `INFORMATION ABOUT TCP TYPES`

## *0 - 9 : service types*

### 0 : re-query in case of fcs distortion

###### order - message id

### 1-2 : registration frames

#### (1) management-client - contains bot token
###### order - message id and bot token.

#### (2) connection-server - answer on current token and successfully connection
###### order - message id, messengerType and botName

### 3 : connection-server - send new message from user. Contains userId, userNickname, messageText
###### order - message id, userId, userNickname and messageText

### 4 : management-client - reply on some message. Contains userId, replyText
###### order - message id, userId and replyText

/*---------------------------------------------------------------------------*\