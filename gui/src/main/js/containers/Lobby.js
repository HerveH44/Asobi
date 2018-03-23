import { connect } from 'react-redux'
import {editGame} from "../reducers/game"
import Lobby from "../components/lobby"

const mapStateToProps = (state) => (state);
const mapDispatchToProps = { editGame };

export default connect(mapStateToProps, mapDispatchToProps)(Lobby);
