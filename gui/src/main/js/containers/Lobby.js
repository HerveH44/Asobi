import { connect } from 'react-redux'
import { editGame, CREATE_GAME } from "../reducers/game"
import Lobby from "../components/lobby"

const mapStateToProps = (state) => (state);
const mapDispatchToProps = { editGame, CREATE_GAME };

export default connect(mapStateToProps, mapDispatchToProps)(Lobby);
