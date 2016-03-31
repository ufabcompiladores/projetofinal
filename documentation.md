# Documentação

## Sintaxe para terminais, não terminais e regras
Símbolos *terminais* devem começar com letras minúsculas. Os caracteres seguintes podem ser letras maiúsculas, minúsculas ou números.

Exemplos:

```
# Símbolos terminais:
aAaA
tERMINAL
t2
a
```

Símbolos *não terminais* devem começar com letras maiúsculas, que podem ser seguidas de letras maiúsculas, minúsculas ou números. Além disso, os caracteres +, -, /, * e ?, sozinhos, também representam não terminais.

Exemplos:

```
# Símbolos não terminais:
AaAa
Terminal
T2
A
+
/
```

Uma regra de produção deve ser escrita na forma

`A -> B | C D ...`
Em que `A` denota um não terminal, e cada símbolo B, C, D, ... pode ser um terminal ou não terminal. Cada símbolo deve ser separado por ao menos um espaço.
Múltiplas regras podem ser agrupadas através do caracter pipe (`|`).
Uma pipe no final da linha indica que o não terminal A produz a cadeia vazia.

Exemplos:

```
# Regras válidas:
A -> B a
B -> b B |

# Regras inválidas e possíveis enganos
// só é válida se BbbB é um símbolo não terminal:
// *não* é a mesma coisa que "B b b B"
B -> BbbB 

B -> B|b
```
