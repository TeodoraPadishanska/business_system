fetch("http://localhost:8080/business/products")
.then(response => response.json())
.then(products => {
    let container = document.getElementById('promo-cards');
    container.innerHTML = ``;

    products.forEach(product => {
        const card = createProductCard(product);
        container.append(card);
        console.log(product);
    })

})
.catch(error => console.log(error));

function calculateDiscount(product) {
    if (product.isOnSale == null){
        product.isOnSale = false;
    }
    if(product.isOnSale || product.price > 0){
        return Math.round(((product.price - product.salePrice) / product.price) * 100);
    }
    return 0;
}
function createProductCard(product) {
    const card = document.createElement('div');
    const discount = calculateDiscount(product)
    card.className = 'card';
    card.style.width = '18rem';

    card.innerHTML = `
        ${product.isOnSale ? `<span class = "discount">- ${discount}%</span>` : ""}
        <button class="favourite-btn">♡</button>
        <img class="card-img-top" src="${product.imgUrl}" alt="${product.name}">
        <div class="card-body">
            <h5 class="card-title">${product.name}</h5>
        </div>
        <div class="price-container">
            <div class="price-row">
                <span class="new-price">
                    ${product.isOnSale ? product.salePrice : product.price}&nbsp;€
                </span>
                ${product.isOnSale ? `<s class="old-price">${product.price}&nbsp;€</s>` : ""}
            </div>
        </div>

    <button class="add_item btn btn-outline-dark" style="margin: 12px">Добави</button>`;
    return card;
}
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

        localStorage.setItem("userId", responseJson.id);
        localStorage.setItem("userFirstName", responseJson.firstName);
        localStorage.setItem("userEmail", responseJson.email);
        localStorage.setItem("userRole", responseJson.role);
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

    const data = {
        email:  email.value,
        password:  password.value,
    }

    console.log("hello1");
    const res = await fetch("http://localhost:8080/business/users/login", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    })
    const responseJson = await res.json();

    console.log("hello");

    if(res.status === 401) {
        if (responseJson.error === "INVALID_PASSWORD") {
            alert(responseJson.message);
        }else if(responseJson.error === "INVALID_EMAIL") {
            alert(responseJson.message);
        }
    }else if(res.ok) {
        alert(responseJson.message);
        console.log(data.id);
        console.log(data.name);
        let modal = bootstrap.Modal.getInstance(document.getElementById("log-in-modal"));
        modal.hide();
        document.getElementById("log-in-form").reset();
    }
}