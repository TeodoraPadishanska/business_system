import {checkAdminStatusNavBtn, checkLoginStatusNavBtn, storeOrderViewId, updateCartPrice} from "./common.js";

checkAdminStatusNavBtn();
checkLoginStatusNavBtn();
updateCartPrice();

const token = localStorage.getItem("token");

async function getOrders() {
    const ordersList = document.getElementById("orders-list");
    ordersList.innerHTML = "";

    await fetch(`http://localhost:8080/business/orders`, {
        method: "GET",
        headers: {
            Authorization: `Bearer ${token}`,
        }
    })
    .then(res =>{
        if (res.status === 200) {
            return res.json();
        }else {
            ordersList.style.textAlign = "center";
            ordersList.innerHTML = `
                <h3>Not logged in.</h3>
            `;
        }
    }).then(orders => {
        if (orders.length === 0) {
            ordersList.innerHTML = "";
            let noOrdersDiv = document.createElement("h1");
            noOrdersDiv.innerHTML = "Нямате направени поръчки."
            noOrdersDiv.className = "ms-5"
            let homeBtn = document.createElement("button");
            homeBtn.className = "btn-style ms-4 m-5";
            homeBtn.id = "home-btn";
            homeBtn.innerHTML = "Начало";
            homeBtn.addEventListener("click", () => {
                window.location.href = "../index.html";
            });
            ordersList.appendChild(noOrdersDiv).appendChild(homeBtn);
        } else {
            ordersList.innerHTML = "";
            ordersList.innerHTML= `
            <div style="display: flex; margin-left: 2vw">
                <img style="background-color: rgb(36 111 106); padding: 10px; border-radius: 10px; height: 50px; margin-right: 20px" src="https://img.icons8.com/?size=100&id=11698&format=png&color=FFFFFF" alt="">
                <h2>Всички поръчки</h2>
            </div>
           `;
            orders.sort((a, b) => b.id - a.id).forEach(order => {

                let orderDiv = document.createElement("div");
                "m-4 container border border-2 rounded p-4 order".split(" ").forEach(styleClass => {
                    orderDiv.classList.add(styleClass)
                });

                let iso = order.orderedOn_date;
                let [year, month, day] = iso.slice(0, 10).split("-");
                let [hour, minute] = iso.slice(11, 17).split(":");

                let formattedDate = `${day}.${month}.${year}`;
                let formattedTime = `${hour}:${minute}`;

                let formattedExpected = "TBD"
                if(order.expectedDelivery){
                    formattedExpected = order.expectedDelivery.split("-").reverse().join(".");
                }

                let orderStatus = document.createElement("div");

                switch (order.orderStatus) {
                    case "PENDING":
                        orderStatus.classList.add("yellow");
                        orderStatus.innerHTML+="PENDING";
                        break;

                    case "PROCESSING":
                        orderStatus.classList.add("light-blue");
                        orderStatus.innerHTML+="PROCESSING";
                        break;

                    case "SHIPPED":
                        orderStatus.classList.add("blue");
                        orderStatus.innerHTML+="SHIPPED";
                        break;

                    case "DELIVERED":
                        orderStatus.classList.add("green");
                        orderStatus.innerHTML+="DELIVERED";
                        break;

                    case "CANCELLED":
                        orderStatus.classList.add("red");
                        orderStatus.innerHTML+="CANCELLED";
                        break;

                    default:
                        orderStatus.classList.add("grey");
                        orderStatus.innerHTML+="TBD";

                }


                orderDiv.innerHTML += `
            <a style="text-decoration: none; color:black" href="order-view.html" id="order-view-${order.id}">
                <div class="orders-view-order">
                    <div style="display: flex">
                        <svg style="margin-right: 1.5vw" xmlns="http://www.w3.org/2000/svg" fill="currentColor" class="bi bi-cart fw-bold order-cart-svg" viewBox="0 0 16 16">
                            <path d="M0 1.5A.5.5 0 0 1 .5 1H2a.5.5 0 0 1 .485.379L2.89 3H14.5a.5.5 0 0 1 .491.592l-1.5 8A.5.5 0 0 1 13 12H4a.5.5 0 0 1-.491-.408L2.01 3.607 1.61 2H.5a.5.5 0 0 1-.5-.5M3.102 4l1.313 7h8.17l1.313-7zM5 12a2 2 0 1 0 0 4 2 2 0 0 0 0-4m7 0a2 2 0 1 0 0 4 2 2 0 0 0 0-4m-7 1a1 1 0 1 1 0 2 1 1 0 0 1 0-2m7 0a1 1 0 1 1 0 2 1 1 0 0 1 0-2"/>
                        </svg>
                        <div>
                            <h3>Поръчка #${String(order.id).padStart(5, '0')}</h3>
                            <div style="display: flex; color: #6b6d7e">
                                <svg style="margin-top: 4px; margin-right: 6px" xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-person" viewBox="0 0 16 16">
                                    <path d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6m2-3a2 2 0 1 1-4 0 2 2 0 0 1 4 0m4 8c0 1-1 1-1 1H3s-1 0-1-1 1-4 6-4 6 3 6 4m-1-.004c-.001-.246-.154-.986-.832-1.664C11.516 10.68 10.289 10 8 10s-3.516.68-4.168 1.332c-.678.678-.83 1.418-.832 1.664z"/>
                                </svg>
                                <p>${order.firstName} ${order.lastName}</p>
                            </div>
                        </div>
                    </div>
                    <div class="orders-dates-address" style="color: rgb(107 109 126 / 1)">
                        <div style="display: flex">
                            <svg style="margin-top: 4px; margin-right: 12px" xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-calendar-event" viewBox="0 0 16 16">
                                <path d="M11 6.5a.5.5 0 0 1 .5-.5h1a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-1a.5.5 0 0 1-.5-.5z"/>
                                <path d="M3.5 0a.5.5 0 0 1 .5.5V1h8V.5a.5.5 0 0 1 1 0V1h1a2 2 0 0 1 2 2v11a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V3a2 2 0 0 1 2-2h1V.5a.5.5 0 0 1 .5-.5M1 4v10a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1V4z"/>
                            </svg>
                            <p>Дата на поръчката: <br><b>${formattedDate} ${formattedTime}</b></p>
                        </div>
                        <div style="display: flex">
                            <svg style="margin-top: 4px; margin-right: 12px" xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-truck" viewBox="0 0 16 16">
                                <path d="M0 3.5A1.5 1.5 0 0 1 1.5 2h9A1.5 1.5 0 0 1 12 3.5V5h1.02a1.5 1.5 0 0 1 1.17.563l1.481 1.85a1.5 1.5 0 0 1 .329.938V10.5a1.5 1.5 0 0 1-1.5 1.5H14a2 2 0 1 1-4 0H5a2 2 0 1 1-3.998-.085A1.5 1.5 0 0 1 0 10.5zm1.294 7.456A2 2 0 0 1 4.732 11h5.536a2 2 0 0 1 .732-.732V3.5a.5.5 0 0 0-.5-.5h-9a.5.5 0 0 0-.5.5v7a.5.5 0 0 0 .294.456M12 10a2 2 0 0 1 1.732 1h.768a.5.5 0 0 0 .5-.5V8.35a.5.5 0 0 0-.11-.312l-1.48-1.85A.5.5 0 0 0 13.02 6H12zm-9 1a1 1 0 1 0 0 2 1 1 0 0 0 0-2m9 0a1 1 0 1 0 0 2 1 1 0 0 0 0-2"/>
                            </svg>
                            <p>Очаквана доставка: <br><b>${formattedExpected ? formattedExpected : "TBD"}</b></p>                                
                        </div>
                        <div style="display: flex">
                            <svg style="margin-top: 4px; margin-right: 12px" xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-geo-alt-fill" viewBox="0 0 16 16">
                                <path d="M8 16s6-5.686 6-10A6 6 0 0 0 2 6c0 4.314 6 10 6 10m0-7a3 3 0 1 1 0-6 3 3 0 0 1 0 6"/>
                            </svg>
                            <p>Адрес: <br><b>${order.address}</b></p>                              
                        </div>
                                               
                    </div>
                    <div class="orders-status-payment" style="padding-right: 5vw" >
                        <p style="width: 120px"  class="${orderStatus.classList.toString()} order-status">${orderStatus.innerHTML}</p>
                        <div style="display: flex">
                            <svg style="margin-right: 12px; margin-top: 4px" xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-credit-card-fill" viewBox="0 0 16 16">
                                <path d="M0 4a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v1H0zm0 3v5a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7zm3 2h1a1 1 0 0 1 1 1v1a1 1 0 0 1-1 1H3a1 1 0 0 1-1-1v-1a1 1 0 0 1 1-1"/>
                            </svg>
                            <p style="color: rgb(92 91 91 / 1)">Плащане: <br><b>${order.paymentStatus}</b></p>
                        </div>                   
                    </div>                       
                    <div>
                        <div class="orders-view-order-price">
                            <p>Total: </p>
                            <h4>${order.orderPrice.toFixed(2)} €</h4>
                        </div>
                    </div>
                    <div style="display: flex; align-self: center">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-chevron-right" viewBox="0 0 16 16">
                            <path fill-rule="evenodd" d="M4.646 1.646a.5.5 0 0 1 .708 0l6 6a.5.5 0 0 1 0 .708l-6 6a.5.5 0 0 1-.708-.708L10.293 8 4.646 2.354a.5.5 0 0 1 0-.708"/>
                        </svg>
                    </div>
                </div>
                
            </a>
        `;

                ordersList.appendChild(orderDiv);
                const orderDivLink = document.getElementById(`order-view-${order.id}`);
                orderDivLink.addEventListener("click", () => {
                    storeOrderViewId(order.id);
                })

            })
        }
    })
}

getOrders();
