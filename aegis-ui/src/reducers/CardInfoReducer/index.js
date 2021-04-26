const initialState = {
  cards: []
}
export default (state = initialState, action) => {
  switch (action.type) {
    case 'ADD_CARDS': return { cards: action.payload }
  }
  return initialState;
}