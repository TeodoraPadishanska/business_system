export default async function dashboard() {


    return `
        <header>
        <div>
            <h1>Dashboard</h1>
            <p>Welcome back, Admin!</p>
        </div>
        <button id="admin-home-btn" onclick="window.location.href = '../index.html'">Home</button>
        <span class="admin-label">Administrator</span>
    </header>

    <section class="stats">
        <article class="stat-card">
            <p>Total Products</p>
            <h2>356</h2>
        </article>

        <article class="stat-card">
            <p>Total Orders</p>
            <h2>1,284</h2>
        </article>

        <article class="stat-card">
            <p>Total Customers</p>
            <h2>849</h2>
        </article>

        <article class="stat-card">
            <p>Total Revenue</p>
            <h2>€24,580</h2>
        </article>
    </section>

    <section class="orders" id="orders">
        <h2>Recent Orders</h2>

        <table>
            <thead>
            <tr>
                <th>Order ID</th>
                <th>Customer</th>
                <th>Total</th>
                <th>Status</th>
            </tr>
            </thead>

            <tbody>
            <tr>
                <td>#1001</td>
                <td>John Smith</td>
                <td>€120.00</td>
                <td><span class="status delivered">Delivered</span></td>
            </tr>

            <tr>
                <td>#1002</td>
                <td>Anna Brown</td>
                <td>€85.50</td>
                <td><span class="status pending">Pending</span></td>
            </tr>

            <tr>
                <td>#1003</td>
                <td>Peter Wilson</td>
                <td>€249.99</td>
                <td><span class="status processing">Processing</span></td>
            </tr>

            <tr>
                <td>#1004</td>
                <td>Maria Ivanova</td>
                <td>€59.00</td>
                <td><span class="status delivered">Delivered</span></td>
            </tr>
            </tbody>
        </table>
    </section>
    `;
}