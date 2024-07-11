const domain = "https://homestaybooking-tbkgshz4sa-uw.a.run.app";
// const domain = "https://staybooking-rww2ycxjgq-uw.a.run.app"; // Eddy's
// https://staybooking-rww2ycxjgq-uw.a.run.app

// 登录函数
export const login = (credential) => {
  const loginUrl = `${domain}/auth/login`;
  // const requestStatus = fetch() // 调用了fetch就是让浏览器发送了一个http请求，返回值不是response，是一个promise（即 请求的状态）
  // // do something right after status become successful (requestStatus == 200 or 400 level, except 500)
  // requestStatus.then(()=>{

  // })
  // 以上连起来写就是：
  return fetch(loginUrl, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(credential), // 自动把格式搞对
  }).then((response) => {
    if (response.status >= 300) {
      throw Error("Fail to log in");
    }
    return response.json(); // 之所以要解析成JSON是因为后端丢出来了一些东西（token等的给前端） 等等
  });
};

export const register = (credential) => {
  const registerUrl = `${domain}/auth/register`;
  return fetch(registerUrl, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(credential),
  }).then((response) => {
    if (response.status >= 300) {
      throw Error("Fail to register");
    }
  });
};

// guest看我有什么reservation; host也可能看对于某个房子的reservation
export const getReservations = () => {
  const authToken = localStorage.getItem("authToken"); // localStorage 是 key : value 格式
  const listReservationsUrl = `${domain}/bookings`;
  return fetch(listReservationsUrl, {
    headers: {
      Authorization: `Bearer ${authToken}`, // Bearer即token的类型
    },
  }).then((response) => {
    if (response.status >= 300) {
      throw Error("Fail to get reservation list");
    }

    return response.json();
  });
};

export const getStaysByHost = () => {
  const authToken = localStorage.getItem("authToken");
  const listStaysUrl = `${domain}/listings`;

  return fetch(listStaysUrl, {
    headers: {
      Authorization: `Bearer ${authToken}`,
    },
  }).then((response) => {
    if (response.status >= 300) {
      throw Error("Fail to get stay list");
    }

    return response.json();
  });
};

// protocal: //domain:port/path?querystringa=a&querystringb=b#anchor

export const searchStays = (query) => {
  const authToken = localStorage.getItem("authToken");
  const searchStaysUrl = new URL(`${domain}/listings/search`);
  searchStaysUrl.searchParams.append("guest_number", query.guest_number); // append函数用于帮忙组装url中?后的部分(即额外携带的参数)；key:value
  searchStaysUrl.searchParams.append(
    "checkin_date",
    query.checkin_date.format("YYYY-MM-DD")
  );
  searchStaysUrl.searchParams.append(
    "checkout_date",
    query.checkout_date.format("YYYY-MM-DD")
  );
  searchStaysUrl.searchParams.append("lat", 37);
  searchStaysUrl.searchParams.append("lon", -122);
  searchStaysUrl.searchParams.append("distance", 500000);

  return fetch(searchStaysUrl, {
    headers: {
      Authorization: `Bearer ${authToken}`,
    },
  }).then((response) => {
    if (response.status >= 300) {
      throw Error("Fail to search stays");
    }

    return response.json();
  });
};

export const deleteStay = (stayId) => {
  const authToken = localStorage.getItem("authToken");
  const deleteStayUrl = `${domain}/listings/${stayId}`;

  return fetch(deleteStayUrl, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${authToken}`,
    },
  }).then((response) => {
    if (response.status >= 300) {
      throw Error("Fail to delete stay");
    }
  });
};

export const bookStay = (data) => {
  const authToken = localStorage.getItem("authToken");
  const bookStayUrl = `${domain}/bookings`;

  return fetch(bookStayUrl, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${authToken}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  }).then((response) => {
    if (response.status >= 300) {
      throw Error("Fail to book reservation");
    }
  });
};

export const cancelReservation = (reservationId) => {
  const authToken = localStorage.getItem("authToken");
  const cancelReservationUrl = `${domain}/bookings/${reservationId}`;

  return fetch(cancelReservationUrl, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${authToken}`,
    },
  }).then((response) => {
    if (response.status >= 300) {
      throw Error("Fail to cancel reservation");
    }
  });
};

export const getReservationsByStay = (stayId) => {
  const authToken = localStorage.getItem("authToken");
  const getReservationByStayUrl = `${domain}/listings/${stayId}/bookings`;

  return fetch(getReservationByStayUrl, {
    headers: {
      Authorization: `Bearer ${authToken}`,
    },
  }).then((response) => {
    if (response.status >= 300) {
      throw Error("Fail to get reservations by stay");
    }

    return response.json();
  });
};

export const uploadStay = (data) => {
  const authToken = localStorage.getItem("authToken");
  const uploadStayUrl = `${domain}/listings`;

  return fetch(uploadStayUrl, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${authToken}`,
    },
    body: data,
  }).then((response) => {
    if (response.status >= 300) {
      throw Error("Fail to upload stay");
    }
  });
};

// how to test functions quickly?
