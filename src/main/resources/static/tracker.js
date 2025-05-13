(function() {

  // Конфигурация
  const API_URL = 'http://localhost:8081/tracker';
  const CLIENT_ID = 1; // Можно динамически получать через meta-тег
  const isStorageAvailable = typeof Storage !== 'undefined';

  // Генерируем sessionId (живет до закрытия вкладки)
  const sessionId = sessionStorage.getItem('tracker_sessionId') || (() => {
    const id = crypto.randomUUID();
    sessionStorage.setItem('tracker_sessionId', id);
    return id;
  })();

  let siteStartTime = performance.now();

  // --- Отправка данных ---
function sendData(eventType, data) {
  const payload = {
    clientId: CLIENT_ID,  // Оставляем в теле для обратной совместимости
    sessionId: sessionId,
    eventType: eventType,
    ...data,
    timestamp: Date.now()
  };

  console.log(`Sending ${eventType} event:`, payload);

  fetch(`${API_URL}/${eventType}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'X-Client-ID': CLIENT_ID.toString()  // Добавляем clientId в заголовок
    },
    body: JSON.stringify(payload)
  })
  .then(response => {
    if (!response.ok) throw new Error(`HTTP error ${response.status}`);
    console.log(`${eventType} event sent successfully`);
  })
  .catch(error => {
    console.error(`Failed to send ${eventType}:`, error);
  });
}

  // --- 1. Трекинг кликов ---
  document.addEventListener('click', (e) => {
    sendData('click', {
      pageUrl: window.location.href,
      x: e.clientX,
      y: e.clientY
    });
  });

  // --- 2. Трекинг скроллов (с троттлингом) ---
  let scrollTimeout;
  window.addEventListener('scroll', () => {
    clearTimeout(scrollTimeout);
    scrollTimeout = setTimeout(() => {
      const scrollDepth = (window.scrollY / (document.body.scrollHeight - window.innerHeight)).toFixed(2);
      sendData('scroll', {
        pageUrl: window.location.href,
        scrollDepth: parseFloat(scrollDepth)
      });
    }, 300);
  });

 // --- 3. Трекинг общего времени на сайте ---
window.addEventListener('beforeunload', () => {
  // 1. Подготовка данных
  const totalSiteTime = Math.round(performance.now() - siteStartTime);
  const isNavigation = isStorageAvailable && sessionStorage.getItem('is_navigation') === 'true';

  // 2. Отправка данных о времени на текущей странице (если это не навигация)
  if (!isNavigation) {
    // Данные для отправки
    const beaconData = {
      clientId: CLIENT_ID,
      eventType: 'pageSession',
      pageUrl: window.location.href,
      timeSpent: totalSiteTime,
      timestamp: Date.now()
    };

    // Попытка отправить через sendBeacon
    const blob = new Blob([JSON.stringify(beaconData)], { type: 'application/json' });
    const beaconSent = navigator.sendBeacon(`${API_URL}/pageSession`, blob);

    // Отображаем статус отправки (для отладки)
    alert(`sendBeacon ${beaconSent ? 'успешно поставлен в очередь' : 'не сработал'}`);

    if (!beaconSent) {
      fetch(`${API_URL}/pageSession`, {
        method: 'POST',
        body: JSON.stringify(beaconData),
        headers: { 'Content-Type': 'application/json' },
        keepalive: true
      })
    }
  }

  if (isStorageAvailable) {
    sessionStorage.removeItem('is_navigation');
  }
});
  // --- 4. Трекинг просмотров страниц ---
 const pagePath = window.location.pathname;
  const startKey = `start_${pagePath}`;
  const pageKey = `viewed_${pagePath}`;
  const navigation = performance.getEntriesByType("navigation")[0];
   if (navigation && navigation.type === "reload") {
     const startTime = parseInt(sessionStorage.getItem(startKey), 10);

     if (!isNaN(startTime)) {
       const timeSpent = Math.round(Date.now() - startTime); // время в мс
       sendData('pageView', {
         pageUrl: window.location.href,
         timeSpent: timeSpent
       });
     }
     sessionStorage.setItem(startKey, Date.now());
   } else {
     sessionStorage.setItem(startKey, Date.now());
   }

  // Для корректного трекинга времени при навигации
  if (isStorageAvailable) {
    window.addEventListener('click', (e) => {
      if (e.target.tagName === 'A' || e.target.closest('a')) {
        sessionStorage.setItem('is_navigation', 'true');
      }
    });
  }
})();