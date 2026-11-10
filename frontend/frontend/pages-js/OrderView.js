import { getOrderViewId } from "./common.js";

const orderId = getOrderViewId();
const token = localStorage.getItem("token");

async function loadOrder(){

    await fetch(`http://localhost:8080/business/orders/${orderId}`, {
        method: "GET",
        headers: {
            Authorization: `Bearer ${token}`,
        }
    }).then((response) => {return response.json()}).then((order) => {
        console.log(order)
        const orderDiv = document.createElement("div");
        orderDiv.innerHTML = `
        <h3>${order.id}</h3>
        <p>${order.firstName} ${order.lastName}</p>
        <p>${order.orderedOn_date}</p>
        <div>
            <p>Status: </p>
            <p>${order.orderStatus}</p>
            <p>Date of delivery: </p>
            <p>${order.expectedDelivery}</p>
            <p>Delivered to: </p>
            <p>${order.address}</p>
            <p class="fw-bold">Total: </p>
            <p>${order.orderPrice}</p>
        </div>
        <div id="order-products"></div>
        `;
        document.getElementById("order-info").appendChild(orderDiv);
        const orderProductsDiv = document.getElementById("order-products");
        order.orderedProducts.forEach(product => {
            console.log(product);
            orderProductsDiv.innerHTML+=`
            <div class="order-product-cart">
                <img src="${product.product.imgUrl}" alt="${product.product.name}">
                <div>
                    <p class="fw-bold">${product.product.name}</p>
                    <p>Unit price: </p>
                    <p>${product.product.price}</p>
                    <p>Quantity: </p>
                    <p>${product.quantity}</p>
                </div>
            </div>
            `;
        })
        orderDiv.appendChild(orderProductsDiv);
        document.getElementById("order-info").appendChild(orderDiv);

    });
}

loadOrder();