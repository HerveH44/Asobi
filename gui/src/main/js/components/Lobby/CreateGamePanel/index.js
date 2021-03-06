import React from "react";
import {
    array,
    string,
    number,
    object,
    bool,
    func
} from "prop-types";
import GameSettings from "./GameSettings";
import {Checkbox} from "../../utils"

class CreatePanel extends React.Component {
    handleCheck = name => event => {
        this.props.editGame({[name]: event.target.checked});
    };

    onChangeValue = name => event => {
        this.props.editGame({[name]: event.target.value});
    };

    onChangeSeats = ({
        currentTarget: {
            value: seats
        }
    }) => {
        this.props.editGame({seats: +seats});
    };

    onChangePacksNumber = ({
        currentTarget: {
            value: packsNumber
        }
    }) => {
        this.props.EDIT_PACK_NUMBER({packsNumber: +packsNumber});
    };

    render() {
        const {
            availableSets,
            createGame,
            sets,
            editGame,
            title,
            seats,
            packsNumber,
            isPrivate,
            gameTypes,
            gameType,
            gameModes,
            gameMode,
            cubeList,
            modernOnly,
            totalChaos
        } = this.props;

        return (
            <fieldset className='fieldset'>
                <legend className='legend'>
                Create a room
                </legend>
                <div>
                    <label>
                    Game title:{" "}
                    <input type='text'
                        value={title}
                        onChange={this.onChangeValue("title")}
                    />
                    </label>
                </div>
                <div>
                    Number of players:{" "}
                    <select value={seats} onChange={this.onChangeSeats}>
                    {[...Array(20).keys()]
                        .map((_, i) => i + 1)
                        .map(x => <option key={x}>{x}</option>)}
                    </select>
                </div>
                <div>
                    Number of packs:{" "}
                    <select value={packsNumber} onChange={this.onChangePacksNumber}>
                    {[...Array(10).keys()]
                        .map((_, i) => i + 1)
                        .map(x => <option key={x}>{x}</option>)}
                    </select>
                </div>
                <div>
                    <Checkbox checked={isPrivate} text='Make room private: ' side='right' onChange={this.handleCheck('isPrivate')}/>
                </div>
                <div>
                    <p>Game type:{" "}
                        <span className='connected-container'>
                        {gameTypes.map((type, key) => (
                            <label key={key} className='radio-label connected-component'>
                            <input
                                className="radio-input connected-component"
                                name='draft-type'
                                type='radio'
                                value={type}
                                onChange={this.onChangeValue("gameType")}
                                checked={gameType === type}/> {type}
                            </label>
                        ))}
                        </span>
                    </p>
                </div>
            <GameSettings
                gameMode={gameMode}
                gameModes={gameModes}
                editGame={editGame}
                gameType={gameType}
                sets={sets}
                availableSets={availableSets}
                packsNumber={packsNumber}
                cubeList={cubeList}
                modernOnly={modernOnly}
                totalChaos={totalChaos}/>
            <p>
                <button onClick={createGame}>
                    Create game
                </button>
            </p>
            </fieldset>
        )
    }
}

CreatePanel.propTypes = {
    title: string.isRequired,
    seats: number.isRequired,
    packsNumber: number.isRequired,
    isPrivate: bool.isRequired,
    gameTypes: array.isRequired,
    gameType: string.isRequired,
    editGame: func.isRequired,
    gameModes: array.isRequired,
    gameMode: string.isRequired,
    sets: object.isRequired,
    availableSets: object.isRequired,
    createGame: func.isRequired,
    EDIT_PACK_NUMBER: func.isRequired,
    cubeList: string.isRequired,
    modernOnly: bool.isRequired,
    totalChaos: bool.isRequired
};

export default CreatePanel;
