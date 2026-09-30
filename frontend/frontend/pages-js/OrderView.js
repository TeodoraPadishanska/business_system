
import {updateCartPrice, checkLoginStatus, getCartPrice} from "./common.js";
checkLoginStatus();


const token = localStorage.getItem("token");
const form = document.getElementById("orderForm");
form.addEventListener("submit", (e) => {

    if (!form.checkValidity()) {
        e.preventDefault();
        form.reportValidity();
        return;
    }


    e.preventDefault();


    const response = fetch("http://localhost:8080/business/orders", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({
            "email": form.email.value,
            "firstName": form.firstName.value,
            "lastName": form.lastName.value,
            "company": form.company.value,
            "address": form.address.value,
            "city": form.city.value,
            "country": form.country.value,
            "postalCode": form.postalCode.value,
            "phoneNumber": form.phoneNumber.value
        })
    })



    if(response.ok) {

        const data = response.json();
        console.log(data);
        alert("Order successful!");
        window.location.href = "/index.html";

    }
    else{
        alert(`Order failed!`);
    }


})



async function loadOrderProducts(){
    await fetch("http://localhost:8080/business/cart", {
        method: "GET",
        headers: {
            Authorization: "Bearer " + localStorage.getItem("token")
        }
    })
    .then(response => response.json())
    .then(data => {
        document.getElementById("main-section").style.display = "block";
        data.forEach((product) => {
            let cartProduct = document.createElement("div");
            const cartItemsDiv = document.getElementById("cart-items");
            cartItemsDiv.classList.add("cartItemsDiv");
            cartProduct.innerHTML = `
            <div>
                <img src="${product.product.imgUrl}" alt="${product.product.name}" alt="${product.image}">
                <p class="ms-3">${product.product.name}</p>
            </div>
          
            <p>x ${product.quantity}</p>
            <p>${product.product.price * product.quantity} €</p>
            `
            cartItemsDiv.appendChild(cartProduct);
        })
    });

    const totalPrice = await getCartPrice();
    if(totalPrice <= 0){
        window.location.href = "/index.html";
    }

    const totalPriceText = document.getElementById("total-euro");
    totalPriceText.textContent +=`${totalPrice} €`;

}


if (!localStorage.getItem("token")) {
    const mainSection = document.getElementById("main-section");
    const message = document.createElement("h3");
    message.textContent = "Влезте в профила си.";
    mainSection.innerHTML = ``;
    mainSection.appendChild(message);
}
else{
    updateCartPrice();
    loadOrderProducts();
}


