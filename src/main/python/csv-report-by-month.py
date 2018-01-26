import csv
import json
from collections import Counter, defaultdict, ChainMap

in_dir = 'input/'
out_dir = 'output/'
def run():
    save_aff_users(in_dir + 'usersGroupByAffiliation.json')
    save_logins('loginsGroupByAffiliation', 'Affiliation')
    save_logins('loginsGroupByApplication', 'Application')


def save_logins(filename, key):
    aff2logins = load_json_as_dict(in_dir + filename + '.json')
    save_csv_logins(aff2logins, out_dir + filename + '.csv', key)


def save_csv_logins(key2logins, filepath, key):
    logins = flat_list(key2logins)
    all_months = sorted({m for m in logins2months(logins)})
    # print(all_months)

    with open(filepath, 'w') as f:
        w = csv.writer(f)
        w.writerow([key, 'Number of all-time logins'] + all_months)
        for aff, logins in key2logins.items():
            month2int = ChainMap(dict(Counter(logins2months(logins))), defaultdict(int))
            row = [aff, len(logins)] + [month2int[m] for m in all_months]
            # print(row)
            w.writerow(row)


def save_aff_users(filepath):
    aff2users = load_json_as_dict(filepath)
    save_csv_user(aff2users)


def save_csv_user(aff2users):
    users = flat_list(aff2users)
    all_months = sorted({m for m in users2months(users)})
    # print(all_months)

    with open(out_dir + 'usersGroupByAffiliation.csv', 'w') as f:
        w = csv.writer(f)
        w.writerow(['Aff', '#'] + all_months)
        for aff, users in aff2users.items():
            month2int = ChainMap(dict(Counter(users2months(users))), defaultdict(int))
            row = [aff, len(users)] + [month2int[m] for m in all_months]
            # print(row)
            w.writerow(row)


def flat_list(aff2users):
    return sum(aff2users.values(), [])


def users2months(users):
    return (u['created_at'][:7] for u in users)


def logins2months(logins):
    return (u['timestamp'][:7] for u in logins)


def load_json_as_dict(filename):
    with open(filename, 'r') as f:
        txt = f.read()
        return json.loads(txt)


run()