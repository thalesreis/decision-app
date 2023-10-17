$(document).ready(function () {
    // Submit the form
    $('#loan-form').submit(function (event) {
        event.preventDefault();

        const personalCode = $('#personalCode').val();
        const loanAmount = parseFloat($('#loanAmount').val());
        const loanPeriod = parseInt($('#loanPeriod').val());

        if (isNaN(loanAmount) || isNaN(loanPeriod)) {
            displayError('Please enter valid loan amount and loan period.');
        } else {
            axios.post('http://localhost:8080/decision-engine/simulate', {
                personalCode: personalCode,
                loanAmount: loanAmount,
                loanPeriod: loanPeriod
            })
            .then(function (response) {
                displayDecisionResponse(response.data.success, response.data);              
            })
            .catch(function (error) {
                console.log(error)
                if (error.response) {
                    displayError(error.response.data.errors.join('<br>'));
                }
                else {
                    displayError(error);
                }
            });
        }
    });

    function displayDecisionResponse(success, data) {
        const resultDiv = $('#result');
        let cardStyle = 'alert-success';
        if (!success) {
            cardStyle = 'alert-warning';
        }

        const cardHtml = `
            <div class="card mt-3 ${cardStyle}">
                <div class="card-body">
                    <h5 class="card-title">${success ? 'Approved' : 'Denied. Try again in the future'}</h5>
                    <p class="card-text">${`Decision Amount: $${data.decisionAmount}`}</p>
                    <p class="card-text">Loan Period: ${data.loanPeriod} months</p>
                </div>
            </div>
        `;
        resultDiv.html(cardHtml);
    }

    function displayError(data) {
        const resultDiv = $('#result');
        const cardHtml = `
            <div class="card mt-3 alert-danger">
                <div class="card-body">
                    <h5 class="card-title">Fail</h5>
                    <p class="card-text">${data}</p>
                </div>
            </div>
        `;
        resultDiv.html(cardHtml);
    }    
});
