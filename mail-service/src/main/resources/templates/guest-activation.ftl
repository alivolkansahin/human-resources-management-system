<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>MUSKETEERS HR Management</title>
</head>
<body style="font-family: Arial, sans-serif; text-align: center; margin: 50px">
    <h1 style="color: #333">Welcome to Musketeers HR System Management ${name}!</h1>
    <p style="color: #666"> Please click button down below to activate your account: ${email} </p>
    <a href="${gatewayUrl}/auth/activate-guest/${id}" target= "_blank" style="cursor: pointer;">
        <button style="padding: 0.75rem 1.25rem; font-size: 1rem; background-color: #4caf50; color: white; border: none; border-radius: 0.25rem;">Activate Account</button>
    </a>
</body>
</html>