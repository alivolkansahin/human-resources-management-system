<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>MUSKETEERS HR Management</title>
</head>
<body style="font-family: Arial, sans-serif; text-align: center; margin: 50px">
<h1 style="color: #333">Hello, ${name} ${lastName}!</h1>
<h2 style="color: #333">Your Spending request dated ${requestCreatedAt} has been processed. Please find the details of your Spending request below:</h2>
<hr>
<p style="color: #666">Description: ${requestDescription} </p>
<p style="color: #666">Amount: ${requestAmount} ${requestCurrency} </p>
<p style="color: #666">Spending Date: ${requestSpendingDate} </p>
<p style="color: #666">Status: ${requestStatus} </p>
<p style="color: #666">Updated At: ${requestUpdatedAt} </p>
</body>
</html>