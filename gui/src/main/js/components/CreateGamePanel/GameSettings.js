import React, { Fragment } from 'react';
import {func, array, string, object} from 'prop-types';
import uniqueId from "lodash/uniqueId";
import "./style.css"

class GameSettings extends React.Component {

    handleChange = name => event => {
        this.props.editGame({[name]: event.target.value})
    };

    handleSet = index => event => {
        this.props.editGame({
            sets: {
                ...this.props.sets,
                [this.props.gameType]: this.props.sets[this.props.gameType]
                                            .map((set, i) => i == index ? event.currentTarget.value : set )
            }
        })
    }

    render() {
        const {availableSets, gameMode, gameType, gameModes, sets} = this.props;

        return (
            <Fragment >
                <div className="tab">
                    {gameModes.map(mode => (
                        <button key={uniqueId()} value={mode} className="tablinks" onClick={this.handleChange("gameMode")}>
                            {mode}
                        </button>))}
                </div>
                {gameMode == "NORMAL" && <NormalGameSettings
                    availableSets={availableSets}
                    sets={sets[gameType]}
                    onChangeSet={this.handleSet.bind(this)}
                    />}
            </Fragment>
        );
    }
}

GameSettings.propTypes = {
    gameModes: array.isRequired,
    gameMode: string.isRequired,
    gameType: string.isRequired,
    editGame: func.isRequired,
    sets: object.isRequired,
    availableSets: object.isRequired
};

const NormalGameSettings = ({availableSets, sets, onChangeSet}) => (
    <div className="tabcontent" >
        {sets.map((set, index) => (<Set
            key={uniqueId()}
            set={set}
            availableSets={availableSets}
            onChangeSet={onChangeSet(index)}/>))}
    </div>
)

NormalGameSettings.propTypes = {
    sets: array.isRequired,
    availableSets: object.isRequired,
    onChangeSet: func.isRequired
};

const Set = ({availableSets, set, onChangeSet}) => (
    <select value={set} onChange={onChangeSet}>
        {Object
            .entries(availableSets)
            .map(([label, sets]) => (
                <optgroup key={uniqueId()} label={label}>
                    {sets.map(({code, name}) => {
                        return <option value={code} key={uniqueId()}>{name}</option>
                    })}
                </optgroup>
            ))}
    </select>
)

Set.propTypes = {
    set: object.isRequired,
    availableSets: object.isRequired,
    onChangeSet: func.isRequired
};

export default GameSettings;
