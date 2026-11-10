
import {
    updateCartPrice,
    checkLoginStatusNavBtn,
    getCartPrice,
    checkBackendHealth,
    checkAdminStatusNavBtn
} from "./common.js";

checkBackendHealth()

checkAdminStatusNavBtn();
checkLoginStatusNavBtn();
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
                emptyCartMessage.textContent = `Количката Ви е празна.`;
                let homeBtn = document.createElement("button");
                homeBtn.className = "btn";
                homeBtn.id="home-btn";
                homeBtn.innerHTML = "Начало";
                homeBtn.addEventListener("click", () => {
                    window.location.href = "../index.html";
                });
                document.getElementById("cart-items").appendChild(emptyCartMessage).appendChild(homeBtn);
            }else{
                data.forEach(product => {
                    cartItemsDiv.append(loadCartProduct(product));

                    const quantityInput = document.getElementById(`product-quantity-${product.product.id}`);
                    quantityInput.addEventListener("change", () => {updateQuantity(product.product.id);});
                })
                const priseAndCheckOutBtn = document.getElementById("priseAndCheckOutBtn");
                priseAndCheckOutBtn.innerHTML='';
                priseAndCheckOutBtn.innerHTML='<h3 id="cart-products-total-price" class="me-4"></h3>\n' +
                    '            <a href="make-order.html" id="go-to-checkout-btn" class="btn btn-style btn-lg" style="color: white">Go to Checkout</a>';
            }
        }).catch(error => console.log(error));

        const totalEuro = await getCartPrice();
        const totalEuroSpan = document.getElementById("cart-products-total-price");
        if (totalEuroSpan){
            totalEuroSpan.textContent = `${totalEuro} €`;
        }

}

if(token){
    loadCart();
}
else{
    let notLoggedInMessage = document.createElement("h4");
    notLoggedInMessage.textContent = `Не сте влязли в профила си или сесията ви е изтекла.`;
    document.getElementById("cart-items").appendChild(notLoggedInMessage);
}




function loadCartProduct(product) {
    let cartItem = document.createElement('div');
    cartItem.setAttribute('class', 'cart-item');
    cartItem.innerHTML = `
        <div class="cart-product" id='cart-product-${product.product.id}'>
            <div>
              <img class="cart-product-img mx-4" src="${product.product.imgUrl}" alt="${product.product.name}">
              <a style="text-decoration: none; color: black" class="product-name">${product.product.name}</a>
            </div>
          
            <div class="product-quantity">
                    <input id="product-quantity-${product.product.id}" 
                           class="product-quantity-input form-control me-4" 
                           type="number"  
                           min="1" 
                           value="${product.quantity}"
                    />
                <div>
                    <span id="product-price-${product.product.id}">${(product.product.price * product.quantity).toFixed(2)}</span>
                </div>
                <div><button type="button" class="btn-close cart-remove-item-btn" aria-label="Close"></button></div>
            </div>              
        </div>`

    const removeProductBtn = cartItem.querySelector(".cart-remove-item-btn");
    removeProductBtn.addEventListener("click", () => {removeProduct(product.product.id)});

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

    productPrice.textContent = (product.product.price * product.quantity).toFixed(2);
    const totalEuro = await getCartPrice();
    const totalEuroSpan = document.getElementById("cart-products-total-price");
    if (totalEuroSpan){
        totalEuroSpan.textContent = `${totalEuro} €`;
    }
    updateCartPrice()

}

async function removeProduct(productId) {
    const product = document.getElementById(`cart-product-${productId}`);
    const response = await fetch(`http://localhost:8080/business/cart/${productId}`,{
        method: "DELETE",
        headers: {
            Authorization: `Bearer ${token}`,
        }
    });
    if (response.status !== 204) {
        alert(response.statusText);
    }else{
        product.parentElement.remove();
    }

    const totalEuroSpan = document.getElementById("cart-products-total-price");
    totalEuroSpan.textContent = await getCartPrice();

    await updateCartPrice();
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
        const totalEuroSpan = document.getElementById("cart-products-total-price");
        totalEuroSpan.innerHTML='';
        cartItemsDiv.appendChild(message).appendChild(homeBtn);
    }
    else{
        const message = document.getElementById('empty-cart-message');
        if(message){
            message.remove();
        }
    }
}

