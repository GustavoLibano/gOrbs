# 🌌 gOrbs - Sistema de Densidade de KI e Orbs para Minecraft

**gOrbs** é um plugin customizado para servidores Minecraft que implementa uma mecânica avançada baseada em **Densidade de KI** e **Orbs**, oferecendo uma nova forma de progressão através de **Training Points (TP)** e múltiplos modificadores dinâmicos.

---

## ⚙️ Funcionalidades

- 🌠 **Densidade de KI Dinâmica**  
  Sistema de densidade que oscila com base no número de jogadores online, podendo fornecer **boosts** ou penalidades no ganho de TP.

- 🔮 **Sistema de Orbs Personalizados**  
  Crie orbs com valores específicos de TP, níveis mínimos para uso e comportamento customizado.

- 🧠 **Boosts e Elementos Especiais**  
  Jogadores com permissões VIP ou elementos (Luz, Água, Escuridão, etc.) ganham bônus adicionais ao utilizar orbs.

- 🗂️ **Logs Automatizados**  
  Todos os usos de orbs são registrados em arquivos YAML separados por jogador.

- 🧩 **Eventos Especiais de Densidade Máxima**  
  Multiplica ainda mais os ganhos durante eventos ativados manualmente.

---

## 📦 Comandos Disponíveis

| Comando                            | Permissão     | Descrição |
|------------------------------------|---------------|-----------|
| `/densidade`                       | Todos         | Abre o painel com informações sobre a Densidade de KI atual. |
| `/criarorb <nome> <valor>`         | `admin.sk`    | Cria uma nova orb com o item atual do jogador. |
| `/editlevelorb <nome> <nivel>`     | `admin.sk`    | Define o nível mínimo necessário para utilizar a orb. |
| `/deletarorb <nome>`               | `admin.sk`    | Remove uma orb do registro. |
| `/giveorb <jogador> <orb> [qtd]`   | `admin.sk`    | Dá uma ou mais orbs ao jogador. |
| `/maxdensity <jogador>`            | `admin.sk`    | Dá o item especial de densidade máxima. |
| `/setmaxdensity`                   | `admin.sk`    | Ativa ou desativa o evento global de Densidade Máxima. |

---

## 📈 Densidade de KI

- **Recalculada automaticamente** a cada X minutos (configurável).
- Pode ter valores negativos ou positivos (malus ou boost).
- Fatores que afetam a densidade:
  - Quantidade de jogadores online.
  - Presença de jogadores VIP.
  - Evento de Densidade Máxima ativo.
- Exemplo:  
  Uma densidade de `+25%` significa 1.25x no valor de TP ganho ao usar uma orb.

---

## 🧪 Boosts, Elementos e Eventos

- **Boosts por Permissões:**
  - `vip.sk`: +25% TP
  - `mvp.sk`: +35% TP
  - `vipmvp.sk`: +45% TP
- **Elementos:**
  - Elementos como `"Luz"`, `"Escuridão"`, `"Água"` etc., aplicam bônus únicos.
- **Eventos Temporários:**
  - `2x Orb`: Dobra o valor de todas as orbs usadas.
  - `Trocador Elemental`: Permite mudar de elemento.

---

## 🧱 Sistema de Orbs

Cada orb possui:

- 📛 **Nome** único
- 🔢 **Valor base de TP**
- 📈 **Nível mínimo** para uso
- 🆔 **ID de registro**
- 🧩 **Customização visual** pelo item utilizado na criação (`/criarorb`)

Ao serem utilizadas:

- 💥 Dão TP com base em todos os multiplicadores
- 💰 Podem dar moedas (dependendo do orb)
- 📜 São **logadas automaticamente** em `/plugins/Logs/TpUsado/`

---

## 🗂️ Estrutura de YAML

---

## ⏱️ Eventos e Recalculo

- A densidade é recalculada periodicamente via `BukkitRunnable`.
- Pode ser sobrescrita ou fixada via `/setmaxdensity`.
- Boosts, elementos e orbs são reprocessados no momento do uso.

---

## 📜 Requisitos

- Servidor **kCauldron 1.7** (recomendado)
- **Java 8+**
- Sistema de permissões (recomendado: **LuckPerms**)
- Pasta `Logs/TpUsado/` com permissão de escrita
- Opcional: Integração com economia (`Vault`)

---

## 🧑‍💻 Autor

Desenvolvido por [Gustavo]  
Inspirado por sistemas de Skript, reimplementado em Java para performance e flexibilidade.

---

## 📄 Licença

Distribuído sob a Licença MIT.  
Você pode usar, modificar e distribuir livremente, desde que mantenha os créditos ao autor.

---

> 💡 Para sugestões, relatórios de bug ou contribuições, abra uma **issue** ou envie um **pull request**!



