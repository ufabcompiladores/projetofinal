function addProduction (tag) {
    tag.previousElementSibling.outerHTML += tag.previousElementSibling.outerHTML;
}

function addRule(tag) {
    var ruleBox = '<input  autocomplete="on"  pattern="^[A-Z][A-Za-z0-9]*"  size="10"  required="required" placeholder="Produtor"  form="grammar"  name="regra"  type="text"> ' +
    '-&gt; ' +
    '<input  autocomplete="on"  pattern="^[a-z][A-Za-z0-9]*"  size="10" required="required"  placeholder="Produção"  name="producao" type="text"> ' +
    '<button  type="button"  onclick="addProduction(this)" name="add-production">Adicionar Produção</button><br>';
    
    tag.previousElementSibling.outerHTML += ruleBox;
}