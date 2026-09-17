
import { updateCartPrice, checkLoginStatus, getCartPrice } from "./common.js";


checkLoginStatus();
updateCartPrice();

const token = localStorage.getItem("token");

async function loadCart() {
    await fetch("http://localhost:8080/business/cart", {
        method: "GET",
        headers: {
            Authorization: `Bearer ${token}`,
        }
    })
        .then(response => {
            if (response.status === 403 || response.status === 401) {


                throw new Error("UNAUTHORIZED");
            }
            if (!response.ok) {
                throw new Error(response.statusText)
            }
            return response.json();
        })
        .then(data => {


            data.sort((a, b) => {return a.product.name.localeCompare(b.product.name);});
            console.log(data);
            console.log(data.items);
            let cartItemsDiv = document.getElementById('cart-items');
            cartItemsDiv.innerHTML = ``;
            if(data.length === 0) {
                let emptyCartMessage = document.createElement("h3");
                emptyCartMessage.textContent = `Количката Ви е праззна.`;
                let homeBtn = document.createElement("button");
                homeBtn.className = "btn";
                homeBtn.id="home-btn";
                homeBtn.innerHTML = "Начало";
                homeBtn.addEventListener("click", () => {
                    window.location.href = "../index.html";
                });
                document.getElementById("cart-items").appendChild(emptyCartMessage).appendChild(homeBtn);
            }else{
                cartItemsDiv.innerHTML = ``;
                data.forEach(product => {
                    cartItemsDiv.append(loadCartProduct(product));
                    console.log(product);
                })
            }
        }).catch(error => console.log(error));

        const totalEuro = await getCartPrice();
        const totalEuroSpan = document.getElementById("total-euro");
        totalEuroSpan.textContent = `${totalEuro} €`;
        // TODO: add total and `Go to Checkout` button

}

if(token){
    loadCart();
}
else{
    let notLoggedInMessage = document.createElement("div");
    notLoggedInMessage.textContent = `Не сте влязли в профила си или сесията ви е изтекла.`;
    document.getElementById("cart-items").appendChild(notLoggedInMessage);
}




function loadCartProduct(product) {
    let cartItem = document.createElement('div');
    cartItem.setAttribute('class', 'cart-item');
    cartItem.innerHTML = `
        <div class="cart-product" id='cart-product-${product.product.id}'>
            <div>
              <img class="cart-product-img mx-4" src="${product.product.imgUrl}" alt="${product.name}">
              <a class="product-name">${product.product.name}</a>
            </div>
          
            <div class="product-quantity">
                <button class="btn product-quantity me-2"></button>
                    <input id="product-quantity-${product.product.id}" 
                           class="product-quantity-input form-control" 
                           type="number"  
                           min="1" 
                           value="${product.quantity}"
                           onChange=updateQuantity(${product.product.id})                    
                    />
                <button class="btn product-quantity"></button>
                <div>
                    <span id="product-price-${product.product.id}">${(product.product.price * product.quantity).toPrecision(2)}</span>
                </div>
                <div><button id="cart-remove-item-btn" type="button" class="btn-close" onclick="removeProduct(${product.product.id})" aria-label="Close"></button></div>
            </div>
            
            
        </div>`
    return cartItem;
}


async function updateQuantity(productId){
    const productInput = document.getElementById(`product-quantity-${productId}`);

    const response = await fetch(`http://localhost:8080/business/cart/${productId}/${productInput.value}`, {
       method: "PATCH",
       headers: {
           Authorization: `Bearer ${token}`,
       }
    });
    if (!response.ok) {
        throw new Error(response.statusText)
    }
    // await loadCart();
    const productPrice = document.getElementById(`product-price-${productId}`);

    const items = await response.json();
    console.log(items);

    const product = items.filter(ci => ci.product.id === productId)[0];
    console.log(product);

    productPrice.textContent = (product.product.price * product.quantity).toPrecision(2);

}

async function removeProduct(productId) {
    const product = document.getElementById(`cart-product-${productId}`);
    const response = await fetch(`http://localhost:8080/business/cart/${productId}`,{
        method: "DELETE",
        headers: {
            Authorization: `Bearer ${token}`,
        }
    });
    if (!response.status === 204) {
        alert(response.statusText);
    }else{

        product.parentElement.remove();
        product.remove();


    }
    await checkEmptyCart();
}




const checkEmptyCart  = async () => {
    let items = document.querySelectorAll('.cart-item');

    console.log(items);

    if(items.length === 0){

        const cartItemsDiv = document.getElementById('cart-items');
        const message = document.createElement("h3");
        message.id = "empty-cart-message";
        message.textContent = "Количката Ви е празна.";

        const homeBtn = document.createElement("button");
        homeBtn.className = "btn";
        homeBtn.id="home-btn";
        homeBtn.innerHTML = "Начало";
        homeBtn.addEventListener("click", () => {
            window.location.href = "../index.html";
        });

        cartItemsDiv.appendChild(message).appendChild(homeBtn);
    }
    else{
        const message = document.getElementById('empty-cart-message');
        if(message){
            message.remove();
        }
    }
}

