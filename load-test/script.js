import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  scenarios: {
    get_candidates: {
      executor: 'constant-vus',
      exec: 'get_candidates',
      vus: 100,
      duration: '30s',
      tags: { my_custom_tag: 'get_candidates' },
      env: { MYVAR: 'get_candidates' },
    },
    make_vote: {
      executor: 'constant-vus',
      exec: 'make_vote',
      vus: 50,
      duration: '30s',
      tags: { my_custom_tag: 'make_vote' },
      env: { MYVAR: 'make_vote' },
    },
    get_votings: {
      executor: 'constant-vus',
      exec: 'get_votings',
      vus: 10,
      duration: '30s',
      tags: { my_custom_tag: 'get_votings' },
      env: { MYVAR: 'get_votings' },
    },
  },
};

export function get_candidates() {
  if (__ENV.MYVAR != 'get_candidates') fail();
  let res = http.get('http://localhost:8090/candidates');
  check(res, { 'status was 200': (r) => r.status == 200 });
}

export function get_votings() {
  if (__ENV.MYVAR != 'get_votings') fail();
  let res = http.get('http://localhost:8090/votings');
  check(res, { 'status was 200': (r) => r.status == 200 });
}

export function make_vote() {
  if (__ENV.MYVAR != 'make_vote') fail();
  const candidate_ids = ['1', '2', '3', '4'];
  const candidate_id = candidate_ids[Math.floor(Math.random() * candidate_ids.length)];

  var url = 'http://localhost:8090/votings/' + candidate_id;
  var payload = JSON.stringify({
    passportId: randomString(12),
  });
  var params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  let res = http.post(url, payload, params);
  check(res, { 'status was 201': (r) => r.status == 201 });
}

function randomString(length) {
  const chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  let result = "";
  for (let i = 0; i < length; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length));
  }
  return result;
}
