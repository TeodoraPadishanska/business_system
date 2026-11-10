import {checkAdminStatusNavBtn, checkLoginStatusNavBtn, getOrderViewId, updateCartPrice} from "./common.js";

updateCartPrice();
checkLoginStatusNavBtn();
checkAdminStatusNavBtn();

const orderId = getOrderViewId();
const token = localStorage.getItem("token");

async function loadOrder(){
    const orderDiv = document.createElement("div");
    orderDiv.innerHTML = "";

    await fetch(`http://localhost:8080/business/orders/${orderId}`, {
        method: "GET",
        headers: {
            Authorization: `Bearer ${token}`,
        }
    }).then(res => {
        if(res.status === 200) {
            return res.json();
        }else{
            orderDiv.innerHTML = `
                <h3>Not logged in</h3>
            `;
            orderDiv.style.textAlign = "center";
            document.getElementById("order-info").appendChild(orderDiv);
        }
    }).then((order) => {
        const iso = order.orderedOn_date;
        const [year, month, day] = iso.slice(0, 10).split("-");
        const [hour, minute] = iso.slice(11, 17).split(":");

        const formattedDate = `${day}.${month}.${year}`;
        const formattedTime = `${hour}:${minute}`;

        let formattedExpected = "TBD"
        if(order.expectedDelivery){
            formattedExpected = order.expectedDelivery.split("-").reverse().join(".");
        }



        orderDiv.innerHTML = `
        <h3>Order number:  #${String(order.id).padStart(5, '0')}</h3>
        <p>${order.firstName} ${order.lastName}</p>
        <p>${formattedDate} ${formattedTime}</p>
        <hr>
        <div>
            <p>Status: </p>
            <p>${order.orderStatus}</p>
            <p>Date of delivery: </p>
            <p>${formattedExpected}</p>
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
                    <p>${product.price}</p>
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