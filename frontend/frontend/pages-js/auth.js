async function createAccount(){
    const email = document.getElementById("email-input-register");
    const password = document.getElementById("password-input-register");
    const repeatPassword = document.getElementById("password-repeat-input-register");
    const firstName = document.getElementById("first-name-register");
    const lastName = document.getElementById("last-name-register");
    const phoneNumber = document.getElementById("phone-number-register");

    if(password.value === repeatPassword.value){
        repeatPassword.style.border = "1px solid lightgray";
        document.getElementById("password-repeat-error").innerHTML = "";
    }else{
        repeatPassword.style.border = "2px solid red";
        document.getElementById("password-repeat-error").innerHTML = "Паролата не съвпада! ";
    }

    let valid = true;

    [email, password, firstName, lastName, phoneNumber].forEach(input => {
        if (input.value.trim() === "") {
            input.style.border = "2px solid red";
            valid = false;
        } else {
            input.style.border = "";
        }
    });

    if (!valid) {
        alert("Попълнете всички полета!");
        return;
    }

    const data = {
        email:  email.value,
        password : password.value,
        firstName : firstName.value,
        lastName : lastName.value,
        phoneNumber : phoneNumber.value
    };

    const res = await fetch("http://localhost:8080/business/users/register", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    });

    const responseJson = await res.json();
    if (res.ok) {
        alert(responseJson.message);
        // alert("Регистрацията е успешна!");
        let modal = bootstrap.Modal.getInstance(document.getElementById("register-modal"));
        modal.hide();
        document.getElementById("register-form").reset();
        document.getElementById("email-register-error").innerHTML = "";
        document.getElementById("phone-register-error").innerHTML = "";

        //imame firstName lastName phoneNumber email
        // localStorage.setItem("userId", responseJson.id);
        // localStorage.setItem("userFirstName", responseJson.firstName);
        // localStorage.setItem("userEmail", responseJson.email);
        // localStorage.setItem("userRole", responseJson.role);
        return;
    }
    if (res.status === 409) {
        if (responseJson.error === "EMAIL_EXISTS") {
            email.style.border = "2px solid red";
            document.getElementById("email-register-error").innerHTML = responseJson.message;
        }else{
            email.style.border = "1px solid lightgray";
            document.getElementById("email-register-error").innerHTML = "";
        }
        if (responseJson.error === "PHONE_EXISTS") {
            phoneNumber.style.border = "2px solid red";
            document.getElementById("phone-register-error").innerHTML = responseJson.message;
        }else{
            phoneNumber.style.border = "1px solid lightgray";
            document.getElementById("phone-register-error").innerHTML = "";
        }
    }
}

async function login(){
    const email = document.getElementById("email-input-log-in");
    const password = document.getElementById("password-input-log-in");

    let valid1 = true;

    [email, password].forEach(input => {

        if (input.value.trim() === "") {
            input.style.border = "2px solid red";
            valid1 = false;
        }else{
            input.style.border = "";
        }
    })
    if (!valid1) {
        alert("Попълнете всички полета.");
        return;
    }

    const data = {
        email:  email.value,
        password:  password.value,
    }



    const res = await fetch("http://localhost:8080/business/users/login", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    })
    const responseJson = await res.json();

    console.log(responseJson);
    localStorage.setItem("token", responseJson.token);

    if(res.ok) {
        alert("Успешен вход.");
        let modal = bootstrap.Modal.getInstance(document.getElementById("log-in-modal"));
        modal.hide();
        document.getElementById("log-in-form").reset();
        location.reload();
    }else {
        alert(responseJson.message);
    }
}



