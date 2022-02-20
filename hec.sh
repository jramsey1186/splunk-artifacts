AUTH_HEADER="Authorization: Splunk "

curl -k https://localhost:8088/service/collector/event -H "$AUTH_HEADER" -d '{BODY="{    \"Title\": \"Sonnet LXX\",    \"LineNumber\": 1,    \"Line\": \"That thou art blamed shall not be thy defect,\",    \"Pattern\": \"A\"  }"}'
curl -k https://localhost:8088/service/collector/event -H "$AUTH_HEADER" -d '{BODY="{    \"Title\": \"Sonnet LXX\",    \"LineNumber\": 2,    \"Line\": \"For slander''s mark was ever yet the fair;\",    \"Pattern\": \"B\"  }"}'
curl -k https://localhost:8088/service/collector/event -H "$AUTH_HEADER" -d '{BODY="{    \"Title\": \"Sonnet LXX\",    \"LineNumber\": 3,    \"Line\": \"The ornament of beauty is suspect,\",    \"Pattern\": \"A\"  }"}'
curl -k https://localhost:8088/service/collector/event -H "$AUTH_HEADER" -d '{BODY="{    \"Title\": \"Sonnet LXX\",    \"LineNumber\": 4,    \"Line\": \"A crow that flies in heaven''s sweetest air.\",    \"Pattern\": \"B\"  }"}'
curl -k https://localhost:8088/service/collector/event -H "$AUTH_HEADER" -d '{BODY="{    \"Title\": \"Sonnet LXX\",    \"LineNumber\": 5,    \"Line\": \"So thou be good, slander doth but approve\",    \"Pattern\": \"C\"  }"}'
curl -k https://localhost:8088/service/collector/event -H "$AUTH_HEADER" -d '{BODY="{    \"Title\": \"Sonnet LXX\",    \"LineNumber\": 6,    \"Line\": \"Thy worth the greater, being woo''d of time;\",    \"Pattern\": \"D\"  }"}'
curl -k https://localhost:8088/service/collector/event -H "$AUTH_HEADER" -d '{BODY="{    \"Title\": \"Sonnet LXX\",    \"LineNumber\": 7,    \"Line\": \"For canker vice the sweetest buds doth love,\",    \"Pattern\": \"C\"  }"}'
curl -k https://localhost:8088/service/collector/event -H "$AUTH_HEADER" -d '{BODY="{    \"Title\": \"Sonnet LXX\",    \"LineNumber\": 8,    \"Line\": \"And thou present''st a pure unstained prime.\",    \"Pattern\": \"D\"  }"}'
curl -k https://localhost:8088/service/collector/event -H "$AUTH_HEADER" -d '{BODY="{    \"Title\": \"Sonnet LXX\",    \"LineNumber\": 9,    \"Line\": \"Thou hast pass''d by the ambush of young days,\",    \"Pattern\": \"E\"  }"}'
curl -k https://localhost:8088/service/collector/event -H "$AUTH_HEADER" -d '{BODY="{    \"Title\": \"Sonnet LXX\",    \"LineNumber\": 10,    \"Line\": \"Either not assail''d or victor being charged;\",    \"Pattern\": \"F\"  }"}'
curl -k https://localhost:8088/service/collector/event -H "$AUTH_HEADER" -d '{BODY="{    \"Title\": \"Sonnet LXX\",    \"LineNumber\": 11,    \"Line\": \"Yet this thy praise cannot be so thy praise,\",    \"Pattern\": \"E\"  }"}'
curl -k https://localhost:8088/service/collector/event -H "$AUTH_HEADER" -d '{BODY="{    \"Title\": \"Sonnet LXX\",    \"LineNumber\": 12,    \"Line\": \"To tie up envy evermore enlarged:\",    \"Pattern\": \"F\"  }"}'
curl -k https://localhost:8088/service/collector/event -H "$AUTH_HEADER" -d '{BODY="{    \"Title\": \"Sonnet LXX\",    \"LineNumber\": 13,    \"Line\": \"If some suspect of ill mask''d not thy show,\",    \"Pattern\": \"G\"  }"}'
curl -k https://localhost:8088/service/collector/event -H "$AUTH_HEADER" -d '{BODY="{    \"Title\": \"Sonnet LXX\",    \"LineNumber\": 14,    \"Line\": \"Then thou alone kingdoms of hearts shouldst owe.\",    \"Pattern\": \"G\"  }"}'