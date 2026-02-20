# Android Demo App

## Overview
このアプリは以下の技術検証を目的として作成しました。
- Clean Architecture
- Jetpack Compose
- Navigation3
- Hilt (DI)
- StateFlow/SharedFlow
- Retrofit/Okhttp
- DataStore

## Architecture
UI / Domain / Data の3層構成

### UI層
- theme：アプリケーションのデザインシステムを定義する。アプリ全体で使用される色（color）、文字スタイル（typography）などを含む。
- widgets：特定の機能画面に依存しない汎用的なUIコンポーネントを格納する。
- feature：各業務画面（UI描画および対応するViewModelを含む）

### Domain層
アーキテクチャの中心であり、他のどの層にも依存しない。 
- interfaces : データアクセスの抽象（インターフェース）を定義。
- model : 純粋なビジネスドメインモデルを定義。
- usecase : 具体的なビジネスロジックや一連の処理フローをカプセル化。

### Data層
全てのデータの取得、保存、管理の責務を負う。
- api : ネットワーク関連コード。network (Retrofit, OkHttp設定)、interfaces (Retrofit API定義) など。
- datastore : AppDataStoreの実装、ローカルデータ永続化を担当。
- repository : domain層で定義されたインターフェースの具象実装クラスを配置。

制御フロー (呼び出し順序): 
`UI (Page)` → `ViewModel` → `UseCase` → `Repository (Interface)` → `RepositoryImp` → `DataSource (API / DataStore)`

依存関係の方向: 
`feature` → `domain` ← `data`
