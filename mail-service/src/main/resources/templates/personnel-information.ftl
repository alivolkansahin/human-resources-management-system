<!DOCTYPE html>
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
        }

        h2 {
            margin-bottom: 10px;
            font-weight: bold;
            color: #007bff;
        }

        h3 {
            margin-bottom: 10px;
            font-weight: bold;
            color: #333;
        }

        p {
            margin-bottom: 8px;
            color: #666;
        }

        hr {
            border: none;
            border-top: 1px solid #ccc;
            margin: 20px 0;
        }

        .footer {
            background-color: #007bff;
            color: #fff;
            padding: 8px 20px;
            text-align: center;
            border-bottom-left-radius: 8px;
            border-bottom-right-radius: 8px;
        }

        .footer p{
            color: #fff;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1 style="font-size: 24px; margin: 0;">MUSKETEERS HR Management</h1>
    </div>
    <div class="content">
        <h2>Hello ${name}, </h2>
        <h3>Welcome to Musketeers HR System Management!</h3>
        <p>You have been successfully added to our system. You can log in to our system with your email address or phone number.</p>
        <br><br>
        <p><strong>Your Email address is:</strong> ${email}</p>
        <p><strong>Your Password is:</strong> ${password}</p>
    </div>
    <div class="footer">
        <p>Thank you for choosing MUSKETEERS HR Management.</p>
    </div>
</div>
</body>
</html>
