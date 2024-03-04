<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MUSKETEERS HR Management</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 600px;
            margin: 50px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
        }

        .header {
            background-color: #007bff;
            color: #fff;
            padding: 20px;
            border-top-left-radius: 8px;
            border-top-right-radius: 8px;
        }

        .header h1 {
            text-align: center;
        }

        .content {
            padding: 20px;
            color: #333;
            text-align: center;
        }

        h1 {
            margin-bottom: 10px;
            font-weight: bold;

        }

        p {
            margin-bottom: 8px;

        }

        a {
            text-decoration: none;
        }

        button {
            padding: 0.75rem 1.25rem;
            font-size: 1rem;
            background-color: #4caf50;
            color: white;
            border: none;
            border-radius: 0.25rem;
            cursor: pointer;
        }

        hr {
            border: none;
            border-top: 1px solid #ccc;
            margin: 20px 0;
        }

        .footer {
            background-color: #007bff;
            color: #fff;
            padding: 5px 10px;
            text-align: center;
            border-bottom-left-radius: 8px;
            border-bottom-right-radius: 8px;
        }
    </style>
</head>

<body>
<div class="container">
    <div class="header">
        <h1 style="font-size: 24px; margin: 0;">MUSKETEERS HR Management</h1>
    </div>
    <div class="content">
        <h1>Welcome to Musketeers HR System Management ${name}!</h1>
        <p>Please click the button below to activate your account: ${email}</p>
        <a href="${gatewayUrl}/auth/activate-guest/${id}" target="_blank">
            <button>Activate Account</button>
        </a>
    </div>
    <div class="footer">
        <p>Thank you for choosing MUSKETEERS HR Management.</p>
    </div>
</div>
</body>
</html>
