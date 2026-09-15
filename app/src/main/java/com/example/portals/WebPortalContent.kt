package com.example.portals

object WebPortalContent {

    fun getEmbeddedHtml(portalType: PortalType): String {
        return when (portalType) {
            PortalType.MARKET -> marketShoppingPortalHtml
            PortalType.INVESTMENT -> investmentDepositPortalHtml
            PortalType.SERVICE, PortalType.JOB -> adminJobPortalHtml
            PortalType.CUSTOM -> customPortalHtml
        }
    }

    private val commonCss = """
        * { box-sizing: border-box; margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; }
        body { background: #0A0A0A; color: #FFFFFF; padding: 16px; -webkit-tap-highlight-color: transparent; }
        .header { display: flex; justify-content: space-between; align-items: center; padding-bottom: 16px; border-bottom: 1px solid #222222; margin-bottom: 16px; }
        .title { font-size: 20px; font-weight: 800; display: flex; align-items: center; gap: 8px; color: #FFFFFF; }
        .badge-yellow { background: rgba(255, 214, 0, 0.15); color: #FFD600; border: 1px solid #FFD600; padding: 4px 10px; border-radius: 12px; font-size: 11px; font-weight: 700; }
        .badge-red { background: rgba(255, 23, 68, 0.15); color: #FF1744; border: 1px solid #FF1744; padding: 4px 10px; border-radius: 12px; font-size: 11px; font-weight: 700; }
        .grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(150px, 1fr)); gap: 12px; margin-bottom: 16px; }
        .card { background: #121212; border: 1px solid #282828; border-radius: 14px; padding: 14px; box-shadow: 0 4px 12px rgba(0,0,0,0.5); }
        .card-title { font-size: 11px; color: #B0B0B0; margin-bottom: 4px; text-transform: uppercase; letter-spacing: 0.8px; font-weight: 600; }
        .card-value { font-size: 22px; font-weight: 800; color: #FFFFFF; }
        .accent-yellow { color: #FFD600; }
        .accent-red { color: #FF1744; }
        .positive { color: #00E676; font-size: 12px; font-weight: 700; display: inline-flex; align-items: center; margin-top: 4px; }
        .section-title { font-size: 16px; font-weight: 800; margin: 18px 0 10px; color: #FFFFFF; display: flex; justify-content: space-between; align-items: center; }
        .btn-yellow { background: #FFD600; color: #000000; border: none; padding: 10px 18px; border-radius: 10px; font-weight: 800; font-size: 13px; cursor: pointer; transition: all 0.15s; display: inline-flex; align-items: center; justify-content: center; gap: 6px; }
        .btn-yellow:active { transform: scale(0.97); opacity: 0.9; }
        .btn-red { background: #FF1744; color: #FFFFFF; border: none; padding: 10px 18px; border-radius: 10px; font-weight: 800; font-size: 13px; cursor: pointer; transition: all 0.15s; display: inline-flex; align-items: center; justify-content: center; gap: 6px; }
        .btn-red:active { transform: scale(0.97); opacity: 0.9; }
        .btn-outline { background: transparent; border: 1px solid #333333; color: #FFFFFF; padding: 8px 14px; border-radius: 8px; font-size: 12px; font-weight: 600; cursor: pointer; }
        .input-group { margin-bottom: 12px; }
        .input-group label { display: block; font-size: 12px; color: #B0B0B0; margin-bottom: 5px; font-weight: 600; }
        .input-group input, .input-group select, .input-group textarea { width: 100%; background: #1C1C1C; border: 1px solid #333333; color: #FFFFFF; padding: 10px; border-radius: 8px; font-size: 14px; outline: none; transition: border-color 0.2s; }
        .input-group input:focus, .input-group select:focus, .input-group textarea:focus { border-color: #FFD600; }
        .tabs { display: flex; gap: 8px; margin-bottom: 14px; overflow-x: auto; padding-bottom: 4px; }
        .tab { background: #1E1E1E; color: #B0B0B0; border: 1px solid #282828; padding: 8px 14px; border-radius: 20px; font-size: 12px; font-weight: 700; cursor: pointer; white-space: nowrap; }
        .tab.active-yellow { background: #FFD600; color: #000000; border-color: #FFD600; }
        .tab.active-red { background: #FF1744; color: #FFFFFF; border-color: #FF1744; }
        .toast { position: fixed; bottom: 20px; left: 50%; transform: translateX(-50%); background: #1E1E1E; border: 1px solid #FFD600; color: #FFFFFF; padding: 10px 20px; border-radius: 24px; font-size: 13px; font-weight: 700; box-shadow: 0 8px 24px rgba(0,0,0,0.8); display: none; z-index: 1000; }
    """.trimIndent()

    // 1. MARKET SHOPPING PORTAL (Full E-Commerce Shopping Experience)
    private val marketShoppingPortalHtml = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
            <title>Market Shopping Portal</title>
            <style>
                $commonCss
                .product-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(150px, 1fr)); gap: 12px; margin-top: 12px; }
                .product-card { background: #141414; border: 1px solid #262626; border-radius: 12px; overflow: hidden; display: flex; flex-direction: column; justify-content: space-between; }
                .product-img-box { width: 100%; height: 110px; background: #1F1F1F; display: flex; align-items: center; justify-content: center; font-size: 42px; position: relative; }
                .discount-tag { position: absolute; top: 8px; left: 8px; background: #FF1744; color: #FFF; font-size: 10px; font-weight: 800; padding: 2px 6px; border-radius: 4px; }
                .product-info { padding: 10px; display: flex; flex-direction: column; flex: 1; justify-content: space-between; }
                .product-name { font-size: 13px; font-weight: 700; color: #FFF; margin-bottom: 4px; line-height: 1.3; }
                .product-price { font-size: 16px; font-weight: 800; color: #FFD600; margin: 6px 0; }
                .cart-drawer { background: #121212; border: 1px solid #FFD600; border-radius: 14px; padding: 14px; margin-top: 16px; }
                .cart-item { display: flex; justify-content: space-between; align-items: center; padding: 8px 0; border-bottom: 1px solid #222; font-size: 13px; }
            </style>
        </head>
        <body>
            <div id="toast" class="toast">Action completed!</div>

            <div class="header">
                <div class="title">
                    <span style="color:#FFD600;">🛒</span> Market Shopping Site
                </div>
                <div class="badge-yellow">STORE LIVE</div>
            </div>

            <!-- Store Stats -->
            <div class="grid">
                <div class="card">
                    <div class="card-title">Shopping Balance</div>
                    <div class="card-value accent-yellow" id="userBalance">$2,450.00</div>
                    <div class="positive">Instant 1-Click Pay</div>
                </div>
                <div class="card">
                    <div class="card-title">Items in Cart</div>
                    <div class="card-value" id="cartCount">0</div>
                    <div style="font-size:12px; color:#B0B0B0; margin-top:4px;" id="cartTotalVal">$0.00 Total</div>
                </div>
            </div>

            <!-- Categories -->
            <div class="tabs">
                <button class="tab active-yellow" onclick="filterCategory(this, 'all')">All Products</button>
                <button class="tab" onclick="filterCategory(this, 'tech')">⚡ Tech & Gadgets</button>
                <button class="tab" onclick="filterCategory(this, 'wear')">👔 Apparel & Style</button>
                <button class="tab" onclick="filterCategory(this, 'hardware')">💻 Pro Hardware</button>
            </div>

            <!-- Shopping Inventory -->
            <div class="section-title">
                <span>Featured Catalog</span>
                <span style="font-size:12px; color:#FFD600; font-weight:600;">Free 24h Express Delivery</span>
            </div>

            <div class="product-grid" id="productGrid">
                <!-- Products rendered via JS -->
            </div>

            <!-- Cart & Checkout Panel -->
            <div class="cart-drawer" id="cartSection" style="margin-top:20px;">
                <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:10px;">
                    <div style="font-size:15px; font-weight:800; color:#FFD600;">🛍️ Shopping Cart & Checkout</div>
                    <button class="btn-outline" onclick="clearCart()">Empty</button>
                </div>
                <div id="cartItemsContainer">
                    <div style="text-align:center; color:#757575; padding:16px; font-size:13px;">Your cart is empty. Add products above!</div>
                </div>
                <div style="display:flex; justify-content:space-between; align-items:center; margin-top:12px; padding-top:10px; border-top:1px solid #333;">
                    <span style="font-size:14px; font-weight:700;">Total Amount:</span>
                    <span style="font-size:20px; font-weight:800; color:#FFD600;" id="cartGrandTotal">$0.00</span>
                </div>
                <button class="btn-yellow" style="width:100%; margin-top:12px;" onclick="checkoutOrder()">
                    <span>💳 Complete Purchase Now</span>
                </button>
            </div>

            <script>
                let balance = 2450.00;
                let cart = [];

                const products = [
                    { id: 1, name: "Quantum Pro Ultra Laptop", price: 1299.00, cat: "hardware", icon: "💻", discount: "-15%" },
                    { id: 2, name: "Noise-Cancelling Studio Pods", price: 189.00, cat: "tech", icon: "🎧", discount: "-25%" },
                    { id: 3, name: "Apex Smartwatch Series 9", price: 299.00, cat: "tech", icon: "⌚", discount: "NEW" },
                    { id: 4, name: "Stealth Black Bomber Jacket", price: 145.00, cat: "wear", icon: "🧥", discount: "-20%" },
                    { id: 5, name: "4K OLED Curved Gaming Rig", price: 649.00, cat: "hardware", icon: "🖥️", discount: "-10%" },
                    { id: 6, name: "Leather Tech Executive Bag", price: 119.00, cat: "wear", icon: "💼", discount: "HOT" },
                    { id: 7, name: "Mechanical RGB Wireless Keybd", price: 89.00, cat: "tech", icon: "⌨️", discount: "-30%" },
                    { id: 8, name: "High-Speed SSD Drive 2TB", price: 139.00, cat: "hardware", icon: "💾", discount: "-18%" }
                ];

                function showToast(msg) {
                    const toast = document.getElementById('toast');
                    toast.innerText = msg;
                    toast.style.display = 'block';
                    setTimeout(() => { toast.style.display = 'none'; }, 2200);
                }

                function renderProducts(filter = 'all') {
                    const container = document.getElementById('productGrid');
                    container.innerHTML = '';
                    const list = filter === 'all' ? products : products.filter(p => p.cat === filter);
                    list.forEach(p => {
                        const card = document.createElement('div');
                        card.className = 'product-card';
                        card.innerHTML = `
                            <div class="product-img-box">
                                <span class="discount-tag">${'$'}{p.discount}</span>
                                <span>${'$'}{p.icon}</span>
                            </div>
                            <div class="product-info">
                                <div>
                                    <div class="product-name">${'$'}{p.name}</div>
                                    <div class="product-price">${'$'}${'$'}{p.price.toFixed(2)}</div>
                                </div>
                                <button class="btn-yellow" style="width:100%; padding:7px 10px; font-size:12px;" onclick="addToCart(${'$'}{p.id})">
                                    + Add to Cart
                                </button>
                            </div>
                        `;
                        container.appendChild(card);
                    });
                }

                function filterCategory(btn, cat) {
                    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active-yellow'));
                    btn.classList.add('active-yellow');
                    renderProducts(cat);
                }

                function addToCart(id) {
                    const product = products.find(p => p.id === id);
                    if (product) {
                        cart.push(product);
                        updateCartUI();
                        showToast(`Added "${'$'}{product.name}" to cart!`);
                    }
                }

                function updateCartUI() {
                    const countEl = document.getElementById('cartCount');
                    const totalValEl = document.getElementById('cartTotalVal');
                    const grandTotalEl = document.getElementById('cartGrandTotal');
                    const container = document.getElementById('cartItemsContainer');

                    countEl.innerText = cart.length;
                    const total = cart.reduce((sum, item) => sum + item.price, 0);
                    totalValEl.innerText = `$${'$'}{total.toFixed(2)} Total`;
                    grandTotalEl.innerText = `$${'$'}{total.toFixed(2)}`;

                    if (cart.length === 0) {
                        container.innerHTML = '<div style="text-align:center; color:#757575; padding:16px; font-size:13px;">Your cart is empty. Add products above!</div>';
                    } else {
                        container.innerHTML = '';
                        cart.forEach((item, index) => {
                            const row = document.createElement('div');
                            row.className = 'cart-item';
                            row.innerHTML = `
                                <span>${'$'}{item.icon} ${'$'}{item.name}</span>
                                <div style="display:flex; align-items:center; gap:8px;">
                                    <span style="color:#FFD600; font-weight:700;">$${'$'}{item.price.toFixed(2)}</span>
                                    <button class="btn-outline" style="padding:2px 6px; color:#FF1744; border-color:#FF1744;" onclick="removeFromCart(${'$'}{index})">✕</button>
                                </div>
                            `;
                            container.appendChild(row);
                        });
                    }
                }

                function removeFromCart(index) {
                    cart.splice(index, 1);
                    updateCartUI();
                }

                function clearCart() {
                    cart = [];
                    updateCartUI();
                    showToast("Cart emptied");
                }

                function checkoutOrder() {
                    if (cart.length === 0) {
                        showToast("Your cart is empty!");
                        return;
                    }
                    const total = cart.reduce((sum, item) => sum + item.price, 0);
                    if (balance < total) {
                        showToast("Insufficient balance! Please top up in the Investment portal.");
                        return;
                    }
                    balance -= total;
                    document.getElementById('userBalance').innerText = `$${'$'}{balance.toFixed(2)}`;
                    cart = [];
                    updateCartUI();
                    showToast(`Order placed successfully! Paid $${'$'}{total.toFixed(2)}.`);
                }

                renderProducts('all');
            </script>
        </body>
        </html>
    """.trimIndent()

    // 2. INVESTMENT & DEPOSIT PORTAL (User deposits & invests anytime with 24/7 returns)
    private val investmentDepositPortalHtml = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
            <title>Investment & Deposit Portal</title>
            <style>
                $commonCss
                .deposit-box { background: #161616; border: 1px solid #FF1744; border-radius: 14px; padding: 16px; margin-bottom: 16px; }
                .invest-card { background: #121212; border: 1px solid #282828; border-radius: 12px; padding: 14px; margin-bottom: 10px; display: flex; justify-content: space-between; align-items: center; }
                .yield-tag { background: rgba(0, 230, 118, 0.15); color: #00E676; padding: 3px 8px; border-radius: 6px; font-weight: 700; font-size: 11px; }
                .active-invest-row { background: #1A1A1A; border-left: 4px solid #FFD600; padding: 10px; margin-bottom: 8px; border-radius: 4px; display: flex; justify-content: space-between; align-items: center; font-size: 13px; }
            </style>
        </head>
        <body>
            <div id="toast" class="toast">Action completed!</div>

            <div class="header">
                <div class="title">
                    <span style="color:#FF1744;">💰</span> Investment & Deposit Hub
                </div>
                <div class="badge-red">24/7 ON-DEMAND INVEST</div>
            </div>

            <!-- Balances Overview -->
            <div class="grid">
                <div class="card">
                    <div class="card-title">Available Cash Balance</div>
                    <div class="card-value accent-yellow" id="cashBalance">$12,850.00</div>
                    <div class="positive">Instant Deposit Ready</div>
                </div>
                <div class="card">
                    <div class="card-title">Total Active Investments</div>
                    <div class="card-value accent-red" id="activeInvestTotal">$45,600.00</div>
                    <div class="positive">▲ +$3,840 (12.4% ROI)</div>
                </div>
            </div>

            <!-- Deposit Funds Panel -->
            <div class="deposit-box">
                <div style="font-size:15px; font-weight:800; color:#FF1744; margin-bottom:10px;">
                    💳 Deposit Funds to Wallet
                </div>
                <div style="display:grid; grid-template-columns: 1fr 1fr; gap:10px; margin-bottom:10px;">
                    <div class="input-group">
                        <label>Deposit Amount ($)</label>
                        <input type="number" id="depositAmount" value="500" min="10" step="50">
                    </div>
                    <div class="input-group">
                        <label>Payment Method</label>
                        <select id="depositMethod">
                            <option value="bank">Direct Bank Transfer (Instant)</option>
                            <option value="card">Debit / Credit Card</option>
                            <option value="crypto">USDT / Crypto Gateway</option>
                        </select>
                    </div>
                </div>
                <button class="btn-red" style="width:100%;" onclick="executeDeposit()">
                    <span>➕ Deposit Funds Now</span>
                </button>
            </div>

            <!-- Invest Anytime Plans -->
            <div class="section-title">
                <span>Available Investment Opportunities (Invest Anytime)</span>
                <span class="badge-yellow">ON-DEMAND</span>
            </div>

            <div class="invest-card">
                <div>
                    <div style="font-size:14px; font-weight:800; color:#FFF;">Alpha Tech Growth Fund</div>
                    <div style="font-size:12px; color:#B0B0B0;">AI, Cloud Computing & Semiconductors</div>
                    <div style="margin-top:4px;"><span class="yield-tag">14.8% APY Expected</span></div>
                </div>
                <button class="btn-yellow" onclick="openInvestModal('Alpha Tech Growth Fund', 14.8)">
                    Invest Now
                </button>
            </div>

            <div class="invest-card">
                <div>
                    <div style="font-size:14px; font-weight:800; color:#FFF;">Prime Real Estate & Infrastructure Yield</div>
                    <div style="font-size:12px; color:#B0B0B0;">Commercial Real Estate & Green Energy</div>
                    <div style="margin-top:4px;"><span class="yield-tag">10.5% APY Fixed</span></div>
                </div>
                <button class="btn-yellow" onclick="openInvestModal('Prime Real Estate Yield', 10.5)">
                    Invest Now
                </button>
            </div>

            <div class="invest-card">
                <div>
                    <div style="font-size:14px; font-weight:800; color:#FFF;">Digital Assets High-Frequency Yield</div>
                    <div style="font-size:12px; color:#B0B0B0;">Automated Liquidity & Arbitrage Pools</div>
                    <div style="margin-top:4px;"><span class="yield-tag">18.2% APY Dynamic</span></div>
                </div>
                <button class="btn-yellow" onclick="openInvestModal('Digital Assets High-Frequency Yield', 18.2)">
                    Invest Now
                </button>
            </div>

            <!-- My Live Investment Positions -->
            <div class="section-title" style="margin-top:20px;">
                <span>My Active Investment Positions</span>
                <button class="btn-outline" onclick="claimDividends()">Claim Returns</button>
            </div>
            <div id="activeInvestList">
                <div class="active-invest-row">
                    <div>
                        <div style="font-weight:700; color:#FFF;">Alpha Tech Growth Fund</div>
                        <div style="font-size:11px; color:#B0B0B0;">Invested: $25,000.00 • Active 32 days</div>
                    </div>
                    <div style="text-align:right;">
                        <div style="font-weight:800; color:#00E676;">+$2,450.00</div>
                        <div style="font-size:10px; color:#FFD600;">+9.8% Yield</div>
                    </div>
                </div>
                <div class="active-invest-row">
                    <div>
                        <div style="font-weight:700; color:#FFF;">Prime Real Estate Yield</div>
                        <div style="font-size:11px; color:#B0B0B0;">Invested: $20,600.00 • Active 14 days</div>
                    </div>
                    <div style="text-align:right;">
                        <div style="font-weight:800; color:#00E676;">+$1,390.00</div>
                        <div style="font-size:10px; color:#FFD600;">+6.7% Yield</div>
                    </div>
                </div>
            </div>

            <script>
                let cashBalance = 12850.00;
                let totalInvested = 45600.00;

                function showToast(msg) {
                    const toast = document.getElementById('toast');
                    toast.innerText = msg;
                    toast.style.display = 'block';
                    setTimeout(() => { toast.style.display = 'none'; }, 2500);
                }

                function executeDeposit() {
                    const amt = parseFloat(document.getElementById('depositAmount').value);
                    if (isNaN(amt) || amt <= 0) {
                        showToast("Please enter a valid deposit amount.");
                        return;
                    }
                    cashBalance += amt;
                    document.getElementById('cashBalance').innerText = `$${'$'}{cashBalance.toFixed(2)}`;
                    showToast(`Successfully deposited $${'$'}{amt.toFixed(2)} to your investment wallet!`);
                }

                function openInvestModal(planName, apy) {
                    const investAmt = prompt(`Invest in "${'$'}{planName}" (${'$'}{apy}% APY)\nEnter investment amount in USD:`, "1000");
                    if (!investAmt) return;
                    const amount = parseFloat(investAmt);
                    if (isNaN(amount) || amount <= 0) {
                        showToast("Invalid investment amount");
                        return;
                    }
                    if (amount > cashBalance) {
                        showToast(`Insufficient cash balance ($${'$'}{cashBalance.toFixed(2)}). Please deposit funds first.`);
                        return;
                    }

                    cashBalance -= amount;
                    totalInvested += amount;
                    document.getElementById('cashBalance').innerText = `$${'$'}{cashBalance.toFixed(2)}`;
                    document.getElementById('activeInvestTotal').innerText = `$${'$'}{totalInvested.toFixed(2)}`;

                    const list = document.getElementById('activeInvestList');
                    const row = document.createElement('div');
                    row.className = 'active-invest-row';
                    row.innerHTML = `
                        <div>
                            <div style="font-weight:700; color:#FFF;">${'$'}{planName}</div>
                            <div style="font-size:11px; color:#B0B0B0;">Invested: $${'$'}{amount.toFixed(2)} • Just started</div>
                        </div>
                        <div style="text-align:right;">
                            <div style="font-weight:800; color:#00E676;">+$0.00</div>
                            <div style="font-size:10px; color:#FFD600;">${'$'}{apy}% APY</div>
                        </div>
                    `;
                    list.prepend(row);
                    showToast(`Successfully placed $${'$'}{amount.toFixed(2)} in ${'$'}{planName}!`);
                }

                function claimDividends() {
                    const reward = 384.50;
                    cashBalance += reward;
                    document.getElementById('cashBalance').innerText = `$${'$'}{cashBalance.toFixed(2)}`;
                    showToast(`Claimed $${'$'}{reward.toFixed(2)} in accrued yields directly to your wallet!`);
                }
            </script>
        </body>
        </html>
    """.trimIndent()

    // 3. ADMIN VERIFIED JOB PORTAL (All available jobs updated & managed by administrator)
    private val adminJobPortalHtml = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
            <title>Admin Job Portal</title>
            <style>
                $commonCss
                .admin-bar { background: #161616; border: 1px solid #FFD600; border-radius: 12px; padding: 12px; margin-bottom: 14px; }
                .job-card { background: #141414; border: 1px solid #262626; border-radius: 12px; padding: 14px; margin-bottom: 12px; transition: border-color 0.2s; }
                .job-card:hover { border-color: #FFD600; }
                .job-header { display: flex; justify-content: space-between; align-items: flex-start; }
                .job-role { font-size: 15px; font-weight: 800; color: #FFF; }
                .job-company { font-size: 12px; color: #FFD600; font-weight: 600; margin-top: 2px; }
                .job-salary { font-size: 13px; font-weight: 800; color: #00E676; margin-top: 4px; }
                .job-meta { display: flex; gap: 8px; flex-wrap: wrap; margin: 8px 0; }
                .meta-tag { background: #1E1E1E; color: #B0B0B0; padding: 3px 8px; border-radius: 4px; font-size: 11px; font-weight: 600; }
                .admin-tag { background: rgba(255, 214, 0, 0.15); color: #FFD600; border: 1px solid #FFD600; padding: 2px 6px; border-radius: 4px; font-size: 10px; font-weight: 800; }
                .modal-overlay { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.85); display: none; align-items: center; justify-content: center; padding: 16px; z-index: 999; }
                .modal-content { background: #121212; border: 1px solid #FFD600; border-radius: 14px; padding: 20px; width: 100%; max-width: 480px; max-height: 90vh; overflow-y: auto; }
            </style>
        </head>
        <body>
            <div id="toast" class="toast">Action completed!</div>

            <div class="header">
                <div class="title">
                    <span style="color:#FFD600;">💼</span> Verified Job Portal
                </div>
                <div class="badge-yellow">UPDATED BY ADMIN</div>
            </div>

            <!-- Admin Notice Banner & Stats -->
            <div class="admin-bar">
                <div style="display:flex; justify-content:space-between; align-items:center;">
                    <div>
                        <span class="admin-tag">ADMINISTRATOR CONTROL</span>
                        <div style="font-size:12px; color:#B0B0B0; margin-top:4px;">All positions are vetted, verified, and updated directly by the platform admin.</div>
                    </div>
                    <button class="btn-yellow" style="padding:6px 12px; font-size:11px;" onclick="openAdminPostModal()">
                        + Post New Job (Admin)
                    </button>
                </div>
            </div>

            <!-- Search and Filter -->
            <div class="input-group">
                <input type="text" id="jobSearch" placeholder="🔍 Search roles, companies, or tech stacks..." onkeyup="filterJobs()">
            </div>

            <!-- Category Filter Tabs -->
            <div class="tabs">
                <button class="tab active-yellow" onclick="filterJobType(this, 'all')">All Jobs (<span id="totalJobCount">6</span>)</button>
                <button class="tab" onclick="filterJobType(this, 'remote')">🌍 100% Remote</button>
                <button class="tab" onclick="filterJobType(this, 'engineering')">💻 Software Engineering</button>
                <button class="tab" onclick="filterJobType(this, 'finance')">📈 Finance & Crypto</button>
                <button class="tab" onclick="filterJobType(this, 'management')">🎯 Product & Leadership</button>
            </div>

            <!-- Jobs List Container -->
            <div id="jobsContainer">
                <!-- Rendered via JS -->
            </div>

            <!-- Admin Job Creation Modal -->
            <div class="modal-overlay" id="adminModal">
                <div class="modal-content">
                    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:14px;">
                        <div style="font-size:16px; font-weight:800; color:#FFD600;">⚡ Administrator: Post Verified Job</div>
                        <button class="btn-outline" onclick="closeAdminPostModal()">✕</button>
                    </div>
                    <div class="input-group">
                        <label>Job Title</label>
                        <input type="text" id="newJobTitle" placeholder="e.g. Lead Android Architect">
                    </div>
                    <div class="input-group">
                        <label>Hiring Company</label>
                        <input type="text" id="newJobCompany" placeholder="e.g. Apex Global Tech">
                    </div>
                    <div class="input-group">
                        <label>Salary Range</label>
                        <input type="text" id="newJobSalary" placeholder="e.g. $140,000 - $185,000 / yr">
                    </div>
                    <div class="input-group">
                        <label>Category</label>
                        <select id="newJobCategory">
                            <option value="engineering">Software Engineering</option>
                            <option value="finance">Finance & Crypto</option>
                            <option value="management">Product & Leadership</option>
                        </select>
                    </div>
                    <div class="input-group">
                        <label>Location & Work Type</label>
                        <input type="text" id="newJobLocation" placeholder="e.g. Remote (Worldwide) or New York, NY">
                    </div>
                    <div class="input-group">
                        <label>Required Skills (comma separated)</label>
                        <input type="text" id="newJobSkills" placeholder="e.g. Kotlin, Jetpack Compose, Coroutines">
                    </div>
                    <div class="input-group">
                        <label>Role Description</label>
                        <textarea id="newJobDesc" rows="3" placeholder="Overview of key responsibilities and perks..."></textarea>
                    </div>
                    <button class="btn-yellow" style="width:100%; margin-top:10px;" onclick="saveAdminJob()">
                        Publish Live Job to Portal
                    </button>
                </div>
            </div>

            <script>
                let jobs = [
                    {
                        id: 1,
                        title: "Senior Android Jetpack Compose Engineer",
                        company: "Quantum Fintech Labs",
                        salary: "$150,000 - $190,000 / yr",
                        category: "engineering",
                        isRemote: true,
                        location: "Remote (US/EU)",
                        skills: ["Kotlin", "Jetpack Compose", "Coroutines", "Clean Architecture"],
                        desc: "Design state-of-the-art mobile trading and investment interfaces with real-time reactive streams.",
                        postedDate: "Updated by Admin: Today"
                    },
                    {
                        id: 2,
                        title: "Principal Quantitative Portfolio Strategist",
                        company: "Apex Capital Partners",
                        salary: "$180,000 - $240,000 + Bonus",
                        category: "finance",
                        isRemote: false,
                        location: "New York, NY",
                        skills: ["Algorithmic Trading", "Python", "Risk Modeling", "Equities"],
                        desc: "Manage high-yield automated capital allocation strategies with algorithmic risk rebalancing.",
                        postedDate: "Updated by Admin: Yesterday"
                    },
                    {
                        id: 3,
                        title: "Lead AI & Machine Learning Architect",
                        company: "Nova Neural Systems",
                        salary: "$165,000 - $210,000 / yr",
                        category: "engineering",
                        isRemote: true,
                        location: "Remote (Worldwide)",
                        skills: ["PyTorch", "LLMs", "Distributed Systems", "GPU Clusters"],
                        desc: "Develop next-generation generative AI agent systems and high-throughput vector pipelines.",
                        postedDate: "Updated by Admin: 2 days ago"
                    },
                    {
                        id: 4,
                        title: "VP of Product Management (Fintech)",
                        company: "Starlight Financial Network",
                        salary: "$175,000 - $225,000 / yr",
                        category: "management",
                        isRemote: true,
                        location: "San Francisco, CA (Hybrid)",
                        skills: ["Product Roadmap", "Growth", "DeFi", "User Experience"],
                        desc: "Lead product development for global payment processing, shopping gateways, and investment portals.",
                        postedDate: "Updated by Admin: 3 days ago"
                    },
                    {
                        id: 5,
                        title: "Senior Full Stack Cloud Engineer",
                        company: "HyperScale Infrastructure",
                        salary: "$140,000 - $175,000 / yr",
                        category: "engineering",
                        isRemote: true,
                        location: "Remote (Global)",
                        skills: ["TypeScript", "Golang", "Kubernetes", "PostgreSQL"],
                        desc: "Scale microservices and event pipelines powering thousands of simultaneous customer checkouts.",
                        postedDate: "Updated by Admin: 3 days ago"
                    },
                    {
                        id: 6,
                        title: "Chief Investment & Treasury Analyst",
                        company: "Global Vanguard Holdings",
                        salary: "$130,000 - $160,000 / yr",
                        category: "finance",
                        isRemote: false,
                        location: "Chicago, IL",
                        skills: ["Treasury Yields", "Macro Economics", "Hedging", "Cashflow"],
                        desc: "Oversee treasury reserve yields, capital deployments, and liquidity optimization.",
                        postedDate: "Updated by Admin: 4 days ago"
                    }
                ];

                let currentCategory = 'all';

                function showToast(msg) {
                    const toast = document.getElementById('toast');
                    toast.innerText = msg;
                    toast.style.display = 'block';
                    setTimeout(() => { toast.style.display = 'none'; }, 2500);
                }

                function renderJobs() {
                    const container = document.getElementById('jobsContainer');
                    const search = document.getElementById('jobSearch').value.toLowerCase();
                    container.innerHTML = '';

                    const filtered = jobs.filter(j => {
                        const matchCat = currentCategory === 'all' || 
                                         (currentCategory === 'remote' && j.isRemote) || 
                                         j.category === currentCategory;
                        const matchSearch = j.title.toLowerCase().includes(search) || 
                                            j.company.toLowerCase().includes(search) || 
                                            j.skills.some(s => s.toLowerCase().includes(search));
                        return matchCat && matchSearch;
                    });

                    document.getElementById('totalJobCount').innerText = jobs.length;

                    if (filtered.length === 0) {
                        container.innerHTML = '<div style="text-align:center; color:#757575; padding:30px; font-size:14px;">No available jobs match your filter criteria.</div>';
                        return;
                    }

                    filtered.forEach(j => {
                        const card = document.createElement('div');
                        card.className = 'job-card';
                        card.innerHTML = `
                            <div class="job-header">
                                <div>
                                    <div class="job-role">${'$'}{j.title}</div>
                                    <div class="job-company">${'$'}{j.company} • <span style="color:#B0B0B0;">${'$'}{j.location}</span></div>
                                </div>
                                <span class="admin-tag">VERIFIED</span>
                            </div>
                            <div class="job-salary">${'$'}{j.salary}</div>
                            <div style="font-size:12px; color:#B0B0B0; margin-top:6px; line-height:1.4;">${'$'}{j.desc}</div>
                            <div class="job-meta">
                                ${'$'}{j.skills.map(s => `<span class="meta-tag">${'$'}{s}</span>`).join('')}
                            </div>
                            <div style="display:flex; justify-content:space-between; align-items:center; margin-top:10px; padding-top:8px; border-top:1px solid #222;">
                                <span style="font-size:11px; color:#757575;">${'$'}{j.postedDate}</span>
                                <button class="btn-yellow" style="padding:6px 14px; font-size:12px;" onclick="applyForJob('${'$'}{j.title}', '${'$'}{j.company}')">
                                    Apply Now ➔
                                </button>
                            </div>
                        `;
                        container.appendChild(card);
                    });
                }

                function filterJobType(btn, cat) {
                    currentCategory = cat;
                    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active-yellow'));
                    btn.classList.add('active-yellow');
                    renderJobs();
                }

                function filterJobs() {
                    renderJobs();
                }

                function applyForJob(title, company) {
                    const applicant = prompt(`Apply for: ${'$'}{title} at ${'$'}{company}\nEnter your Full Name and Contact Email:`, "Alex Carter (alex.carter@example.com)");
                    if (applicant) {
                        showToast(`Application for "${'$'}{title}" submitted to Admin review!`);
                    }
                }

                function openAdminPostModal() {
                    document.getElementById('adminModal').style.display = 'flex';
                }

                function closeAdminPostModal() {
                    document.getElementById('adminModal').style.display = 'none';
                }

                function saveAdminJob() {
                    const title = document.getElementById('newJobTitle').value.trim();
                    const company = document.getElementById('newJobCompany').value.trim();
                    const salary = document.getElementById('newJobSalary').value.trim();
                    const category = document.getElementById('newJobCategory').value;
                    const location = document.getElementById('newJobLocation').value.trim();
                    const skillsStr = document.getElementById('newJobSkills').value.trim();
                    const desc = document.getElementById('newJobDesc').value.trim();

                    if (!title || !company || !salary) {
                        showToast("Please fill in the required Job Title, Company, and Salary fields.");
                        return;
                    }

                    const skills = skillsStr ? skillsStr.split(',').map(s => s.trim()) : ["Engineering", "Leadership"];
                    const isRemote = location.toLowerCase().includes("remote");

                    const newJob = {
                        id: jobs.length + 1,
                        title: title,
                        company: company,
                        salary: salary,
                        category: category,
                        isRemote: isRemote,
                        location: location || "Remote",
                        skills: skills,
                        desc: desc || "Administrator verified full-time high-impact position.",
                        postedDate: "Updated by Admin: Just now"
                    };

                    jobs.unshift(newJob);
                    closeAdminPostModal();
                    renderJobs();
                    showToast(`Admin published job "${'$'}{title}" to live portal!`);
                }

                renderJobs();
            </script>
        </body>
        </html>
    """.trimIndent()

    private val customPortalHtml = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
            <title>Web Explorer</title>
            <style>
                $commonCss
            </style>
        </head>
        <body>
            <div class="header">
                <div class="title">
                    <span style="color:#FF1744;">🌐</span> Global Portal Explorer
                </div>
                <div class="badge-red">CONNECTED</div>
            </div>
            <div class="card">
                <div class="card-title">Portal Gateway</div>
                <div style="font-size:14px; margin-top:8px; line-height:1.5; color:#B0B0B0;">
                    Switch between the <b>Market Shopping Portal</b>, <b>Investment & Deposit Hub</b>, and <b>Admin Verified Job Portal</b> to explore comprehensive live operations.
                </div>
            </div>
        </body>
        </html>
    """.trimIndent()
}
