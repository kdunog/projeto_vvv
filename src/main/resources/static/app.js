const baseUrl = '';

function showSection(id) {
    document.querySelectorAll('.panel').forEach(panel => panel.classList.add('hidden'));
    document.getElementById(id).classList.remove('hidden');
    clearMessage();
    if (id === 'passageiros') loadPassageiros();
    if (id === 'cidades') loadCidades();
    if (id === 'transportadoras') loadTransportadoras();
    if (id === 'modais') {
        loadModais();
        loadTransportadoraOptions();
    }
    if (id === 'reservas') {
        loadReservas();
        loadPassageiroOptions();
        loadCidadeOptions();
        loadModalOptions();
    }
    if (id === 'pagamentos') {
        loadPagamentos();
        loadReservaOptions();
    }
    if (id === 'enderecos') {
        loadEnderecos();
        loadCidadeOptions();
    }
    if (id === 'pontos-venda') {
        loadPontosVenda();
        loadEnderecoOptions();
    }
    if (id === 'funcionarios') {
        loadFuncionarios();
        loadEnderecoOptions();
        loadPontoVendaOptions();
    }
    if (id === 'tickets') {
        loadTickets();
        loadReservaOptions();
    }
}

function showMessage(text, type = 'success') {
    const message = document.getElementById('message');
    message.textContent = text;
    message.className = `message ${type}`;
    message.style.display = 'block';
}

function clearMessage() {
    const message = document.getElementById('message');
    message.textContent = '';
    message.style.display = 'none';
    message.className = 'message';
}

async function request(path, options = {}) {
    try {
        const response = await fetch(`${baseUrl}${path}`, options);
        const text = await response.text();
        let data = null;
        if (text) {
            try {
                data = JSON.parse(text);
            } catch {
                data = text;
            }
        }
        if (!response.ok) {
            throw new Error(data?.message || text || response.statusText);
        }
        return data;
    } catch (error) {
        showMessage(error.message, 'error');
        throw error;
    }
}

async function loadPassageiros() {
    const list = document.getElementById('passageiros-list');
    list.innerHTML = '<li>Carregando...</li>';
    const passageiros = await request('/passageiros');
    list.innerHTML = passageiros.map(p => `<li>${p.id} - ${p.nome} (${p.cpf}) - ${p.email || 'sem email'}</li>`).join('');
}

async function createPassageiro(event) {
    event.preventDefault();
    const form = event.target;
    const payload = {
        nome: form.nome.value,
        cpf: form.cpf.value,
        telefone: form.telefone.value,
        email: form.email.value,
        senha: form.senha.value,
        idade: parseInt(form.idade.value, 10)
    };
    await request('/passageiros', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    form.reset();
    showMessage('Passageiro criado com sucesso.');
    loadPassageiros();
}

async function loadCidades() {
    const list = document.getElementById('cidades-list');
    list.innerHTML = '<li>Carregando...</li>';
    const cidades = await request('/cidades');
    list.innerHTML = cidades.map(c => `<li>${c.id} - ${c.nome} [${c.indentificador}] - ${c.estado}</li>`).join('');
}

async function createCidade(event) {
    event.preventDefault();
    const form = event.target;
    const payload = {
        nome: form.nome.value,
        indentificador: form.indentificador.value,
        estado: form.estado.value
    };
    await request('/cidades', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    form.reset();
    showMessage('Cidade criada com sucesso.');
    loadCidades();
}

function getModalDisponibilidade(modal, reservas) {
    const capacidadeMaxima = Number(modal.capacidade) || 0;
    const capacidadeAtual = reservas.filter(reserva => Number(reserva.modal?.id) === Number(modal.id)).length;
    const disponibilidade = capacidadeAtual >= capacidadeMaxima ? 'Indisponível' : 'Disponível';
    return {
        capacidadeAtual,
        capacidadeMaxima,
        disponibilidade
    };
}

async function loadModais() {
    const list = document.getElementById('modais-list');
    list.innerHTML = '<li>Carregando...</li>';
    const [modais, reservas] = await Promise.all([
        request('/modais'),
        request('/reservas')
    ]);

    list.innerHTML = modais.map(m => {
        const { capacidadeAtual, capacidadeMaxima, disponibilidade } = getModalDisponibilidade(m, reservas);
        return `<li>${m.id} - ${m.tipo} (${capacidadeAtual}/${capacidadeMaxima}) - ${disponibilidade} - Transportadora: ${m.transportadora?.nome || 'N/A'}</li>`;
    }).join('');
}

async function loadTransportadoras() {
    const list = document.getElementById('transportadoras-list');
    list.innerHTML = '<li>Carregando...</li>';
    const transportadoras = await request('/transportadoras');
    list.innerHTML = transportadoras.map(t => `<li>${t.id} - ${t.nome} (${t.cnpj})</li>`).join('');
}

async function loadTransportadoraOptions() {
    const select = document.getElementById('transportadoraId');
    const transportadoras = await request('/transportadoras');
    if (!transportadoras.length) {
        select.innerHTML = '<option value="">Cadastre uma transportadora primeiro</option>';
        return;
    }
    select.innerHTML = transportadoras.map(t => `<option value="${t.id}">${t.id} - ${t.nome}</option>`).join('');
}

async function createTransportadora(event) {
    event.preventDefault();
    const form = event.target;
    const payload = {
        nome: form.nome.value,
        cnpj: form.cnpj.value
    };
    await request('/transportadoras', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    form.reset();
    showMessage('Transportadora criada com sucesso.');
    loadTransportadoras();
    loadTransportadoraOptions();
}

async function createModal(event) {
    event.preventDefault();
    const form = event.target;
    const payload = {
        tipo: form.tipo.value,
        capacidade: parseInt(form.capacidade.value, 10),
        ultimaManutencao: form.ultimaManutencao.value || null,
        transportadora: { id: parseInt(form.transportadoraId.value, 10) }
    };
    if (!payload.ultimaManutencao) delete payload.ultimaManutencao;
    await request('/modais', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    form.reset();
    showMessage('Modal criado com sucesso.');
    loadModais();
}

async function loadReservaOptions() {
    const select = document.getElementById('pagamentoReservaId');
    if (!select) return;
    const reservas = await request('/reservas');
    if (!Array.isArray(reservas)) {
        showMessage('Erro ao carregar reservas.', 'error');
        select.innerHTML = '<option value="">Nenhuma reserva disponível</option>';
        return;
    }
    select.innerHTML = reservas.map(r => `<option value="${r.id}">${r.id} - ${r.passageiro?.nome || 'N/A'} - ${r.status || 'N/A'}</option>`).join('');
}

async function loadPassageiroOptions() {
    const select = document.getElementById('passageiroId');
    const passageiros = await request('/passageiros');
    select.innerHTML = passageiros.map(p => `<option value="${p.id}">${p.id} - ${p.nome}</option>`).join('');
}

async function loadCidadeOptions() {
    const origemSelect = document.getElementById('origemId');
    const destinoSelect = document.getElementById('destinoId');
    const cidadeSelect = document.getElementById('cidadeId');
    const enderecoCidadeSelect = document.getElementById('endereçoCidadeId');
    const cidades = await request('/cidades');
    const options = cidades.map(c => `<option value="${c.id}">${c.id} - ${c.nome}</option>`).join('');
    if (origemSelect) origemSelect.innerHTML = options;
    if (destinoSelect) destinoSelect.innerHTML = options;
    if (cidadeSelect) cidadeSelect.innerHTML = options;
    if (enderecoCidadeSelect) enderecoCidadeSelect.innerHTML = options;
}

async function loadModalOptions() {
    const select = document.getElementById('modalId');
    const modais = await request('/modais');
    select.innerHTML = modais.map(m => `<option value="${m.id}">${m.id} - ${m.tipo}</option>`).join('');
}

async function loadEnderecoOptions() {
    const selects = [
        document.getElementById('enderecoId'),
        document.getElementById('pontoVendaEnderecoId'),
        document.getElementById('funcionarioEnderecoId')
    ].filter(Boolean);

    if (!selects.length) {
        return;
    }

    const enderecos = await request('/enderecos');
    const options = enderecos.map(e => `<option value="${e.id}">${e.id} - ${e.logradouro}, ${e.numero}</option>`).join('');
    selects.forEach(select => select.innerHTML = options);
}

async function loadPontoVendaOptions() {
    const select = document.getElementById('funcionarioPontoVendaId');
    if (!select) {
        return;
    }
    const pontos = await request('/pontos-venda');
    if (!pontos.length) {
        select.innerHTML = '<option value="">Cadastre um ponto de venda primeiro</option>';
        return;
    }
    select.innerHTML = pontos.map(p => `<option value="${p.id}">${p.id} - ${p.cnpj || p.telefone || 'Ponto'}</option>`).join('');
}

async function loadReservas() {
    const list = document.getElementById('reservas-list');
    list.innerHTML = '<li>Carregando...</li>';
    const reservas = await request('/reservas');
    list.innerHTML = reservas.map(r => `<li>${r.id} - Passageiro ${r.passageiro?.id || 'N/A'} > ${r.cidadeOrigem?.nome || 'N/A'} -> ${r.cidadeDestino?.nome || 'N/A'} - ${r.status || 'sem status'}</li>`).join('');
}

async function createReserva(event) {
    event.preventDefault();
    const form = event.target;
    const payload = {
        passageiro: { id: parseInt(form.passageiroId.value, 10) },
        acompanhante: form.acompanhanteId.value ? { id: parseInt(form.acompanhanteId.value, 10) } : null,
        cidadeOrigem: { id: parseInt(form.origemId.value, 10) },
        cidadeDestino: { id: parseInt(form.destinoId.value, 10) },
        modal: { id: parseInt(form.modalId.value, 10) },
        dataReserva: form.dataReserva.value,
        vendaOnline: form.vendaOnline.value === 'true',
        tipoVenda: form.tipoVenda.value
    };
    await request('/reservas', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    form.reset();
    showMessage('Reserva criada com sucesso.');
    await Promise.all([
        loadReservas(),
        loadModais()
    ]);
}

async function loadPagamentos() {
    const list = document.getElementById('pagamentos-list');
    list.innerHTML = '<li>Carregando...</li>';
    const pagamentos = await request('/pagamentos');
    list.innerHTML = pagamentos.map(p => `<li>${p.id} - Reserva ${p.reserva?.id || 'N/A'} - Valor: R$ ${p.valor} - Status: ${p.status || 'sem status'}</li>`).join('');
}

function updatePagamentoFields() {
    const metodo = document.getElementById('metodoPagamento').value;
    const creditoFields = document.getElementById('creditoFields');
    const debitoFields = document.getElementById('debitoFields');
    const boletoInfo = document.getElementById('boletoInfo');
    const pixInfo = document.getElementById('pixInfo');
    const pagamentoInfo = document.getElementById('pagamentoInfo');
    const numeroParcelas = document.getElementById('numeroParcelas');
    const numeroCartao = document.getElementById('numeroCartao');
    const cvv = document.getElementById('cvv');
    const dataValidade = document.getElementById('dataValidade');
    const numeroCartaoDebito = document.getElementById('numeroCartaoDebito');
    const cvvDebito = document.getElementById('cvvDebito');
    const dataValidadeDebito = document.getElementById('dataValidadeDebito');

    creditoFields.classList.toggle('hidden', metodo !== 'CARTAO_CREDITO');
    debitoFields.classList.toggle('hidden', metodo !== 'CARTAO_DEBITO');
    boletoInfo.classList.toggle('hidden', metodo !== 'BOLETO');
    pixInfo.classList.toggle('hidden', metodo !== 'PIX');

    if (metodo === 'CARTAO_CREDITO') {
        pagamentoInfo.textContent = 'Informe os dados do cartão de crédito e o número de parcelas (máx. 12). Juros de 5% a partir de 4 parcelas.';
        numeroParcelas.required = true;
        numeroCartao.required = true;
        cvv.required = true;
        dataValidade.required = true;
        numeroCartaoDebito.required = false;
        cvvDebito.required = false;
        dataValidadeDebito.required = false;
    } else if (metodo === 'CARTAO_DEBITO') {
        pagamentoInfo.textContent = 'Informe os dados do cartão de débito. Nenhuma parcela é necessária.';
        numeroParcelas.required = false;
        numeroParcelas.value = '';
        numeroCartao.required = false;
        cvv.required = false;
        dataValidade.required = false;
        numeroCartaoDebito.required = true;
        cvvDebito.required = true;
        dataValidadeDebito.required = true;
    } else {
        pagamentoInfo.textContent = 'Selecione a forma de pagamento para ver os campos adicionais.';
        numeroParcelas.required = false;
        numeroParcelas.value = '';
        numeroCartao.required = false;
        cvv.required = false;
        dataValidade.required = false;
        numeroCartaoDebito.required = false;
        cvvDebito.required = false;
        dataValidadeDebito.required = false;
    }
}

function updateJurosInfo() {
    const parcelas = parseInt(document.getElementById('numeroParcelas').value, 10);
    const valor = parseFloat(document.getElementById('pagamento-form').valor.value || 0);
    const jurosInfo = document.getElementById('jurosInfo');
    const valorTotalInfo = document.getElementById('valorTotalInfo');
    
    if (!parcelas || parcelas < 1) {
        jurosInfo.textContent = 'Sem juros até 3 parcelas; 5% de juros a partir de 4 parcelas.';
        valorTotalInfo.classList.add('hidden');
        return;
    }
    
    if (parcelas < 4) {
        const valorParcela = (valor / parcelas).toFixed(2);
        jurosInfo.textContent = `Sem juros: valor por parcela = R$ ${valorParcela}`;
        valorTotalInfo.classList.add('hidden');
    } else {
        const valorComJuros = valor * 1.05;
        const valorParcela = (valorComJuros / parcelas).toFixed(2);
        jurosInfo.textContent = `Juros aplicáveis: 5%`;
        valorTotalInfo.textContent = `Valor total com juros: R$ ${valorComJuros.toFixed(2)} | Valor por parcela: R$ ${valorParcela}`;
        valorTotalInfo.classList.remove('hidden');
    }
}

async function createPagamento(event) {
    event.preventDefault();
    const form = event.target;
    const metodoPagamento = form.metodoPagamento.value;
    const valor = parseFloat(form.valor.value);
    const reservaId = parseInt(form.reservaId.value, 10);
    
    // Validações básicas
    if (!reservaId) {
        showMessage('Selecione uma reserva.', 'error');
        return;
    }
    if (!valor || valor <= 0) {
        showMessage('Valor deve ser maior que zero.', 'error');
        return;
    }

    // Validações por tipo de pagamento
    let numeroParcelas = 1;
    if (metodoPagamento === 'CARTAO_CREDITO') {
        numeroParcelas = parseInt(form.numeroParcelas.value, 10);
        if (!numeroParcelas || numeroParcelas < 1 || numeroParcelas > 12) {
            showMessage('Parcelas devem ser entre 1 e 12.', 'error');
            return;
        }
        if (!form.numeroCartao.value || form.numeroCartao.value.length < 13) {
            showMessage('Número do cartão inválido.', 'error');
            return;
        }
        if (!form.cvv.value || form.cvv.value.length < 3) {
            showMessage('CVV inválido.', 'error');
            return;
        }
        if (!form.dataValidade.value || !/^\d{2}\/\d{2}$/.test(form.dataValidade.value)) {
            showMessage('Data de validade inválida (use MM/AA).', 'error');
            return;
        }
    } else if (metodoPagamento === 'CARTAO_DEBITO') {
        if (!form.numeroCartaoDebito.value || form.numeroCartaoDebito.value.length < 13) {
            showMessage('Número do cartão inválido.', 'error');
            return;
        }
        if (!form.cvvDebito.value || form.cvvDebito.value.length < 3) {
            showMessage('CVV inválido.', 'error');
            return;
        }
        if (!form.dataValidadeDebito.value || !/^\d{2}\/\d{2}$/.test(form.dataValidadeDebito.value)) {
            showMessage('Data de validade inválida (use MM/AA).', 'error');
            return;
        }
    }

    // Montar o payload
    const payload = {
        reserva: { id: reservaId },
        valor: valor,
        numeroDeParcelas: numeroParcelas,
        metodoPagamento: metodoPagamento
    };

    await request('/pagamentos', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    form.reset();
    updatePagamentoFields();
    showMessage('Pagamento processado com sucesso.');
    loadPagamentos();
}

async function loadEnderecos() {
    const list = document.getElementById('enderecos-list');
    list.innerHTML = '<li>Carregando...</li>';
    const enderecos = await request('/enderecos');
    list.innerHTML = enderecos.map(e => `<li>${e.id} - ${e.logradouro}, ${e.numero} - ${e.bairro || ''} - ${e.cep || ''} - ${e.cidade?.nome || 'N/A'}</li>`).join('');
}

async function createEndereco(event) {
    event.preventDefault();
    const form = event.target;
    const payload = {
        cidade: { id: parseInt(form.cidadeId.value, 10) },
        logradouro: form.logradouro.value,
        numero: parseInt(form.numero.value, 10),
        complemento: form.complemento.value,
        bairro: form.bairro.value,
        cep: form.cep.value
    };
    await request('/enderecos', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    form.reset();
    showMessage('Endereço criado com sucesso.');
    loadEnderecos();
}

async function loadPontosVenda() {
    const list = document.getElementById('pontos-venda-list');
    list.innerHTML = '<li>Carregando...</li>';
    const pontos = await request('/pontos-venda');
    list.innerHTML = pontos.map(p => `<li>${p.id} - ${p.cnpj || 'sem CNPJ'} - ${p.telefone || 'sem telefone'} - Endereço: ${p.endereco?.logradouro || 'N/A'}</li>`).join('');
}

async function createPontoVenda(event) {
    event.preventDefault();
    const form = event.target;
    const payload = {
        endereco: { id: parseInt(form.pontoVendaEnderecoId.value, 10) },
        telefone: form.telefone.value,
        cnpj: form.cnpj.value
    };
    await request('/pontos-venda', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    form.reset();
    showMessage('Ponto de venda criado com sucesso.');
    loadPontosVenda();
}

async function loadFuncionarios() {
    const list = document.getElementById('funcionarios-list');
    list.innerHTML = '<li>Carregando...</li>';
    const funcionarios = await request('/funcionarios');
    list.innerHTML = funcionarios.map(f => `<li>${f.id} - ${f.nome} (${f.cpf}) - Cargo: ${f.cargo || 'N/A'} - Endereço: ${f.enderecoResidencia?.logradouro || 'N/A'}</li>`).join('');
}

async function createFuncionario(event) {
    event.preventDefault();
    const form = event.target;
    const selectedOptions = Array.from(form.pontosVenda.selectedOptions).map(opt => ({ id: parseInt(opt.value, 10) }));
    const payload = {
        nome: form.nome.value,
        cpf: form.cpf.value,
        telefone: form.telefone.value,
        email: form.email.value,
        senha: form.senha.value,
        enderecoResidencia: { id: parseInt(form.enderecoId.value, 10) },
        pontosVenda: selectedOptions,
        autorizadoMultiplosPontos: form.autorizadoMultiplosPontos.value === 'true'
    };
    await request('/funcionarios', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    form.reset();
    showMessage('Funcionário criado com sucesso.');
    loadFuncionarios();
}

async function loadTickets() {
    const list = document.getElementById('tickets-list');
    list.innerHTML = '<li>Carregando...</li>';
    const tickets = await request('/tickets');
    list.innerHTML = tickets.map(t => `<li>${t.id} - Reserva ${t.reserva?.id || 'N/A'} - Valor: R$ ${t.valor}</li>`).join('');
}

async function emitirTicket(event) {
    event.preventDefault();
    const form = event.target;
    const payload = { id: parseInt(form.reservaId.value, 10) };
    await request('/tickets/emitir', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    form.reset();
    showMessage('Ticket emitido com sucesso.');
    loadTickets();
}

showSection('passageiros');
